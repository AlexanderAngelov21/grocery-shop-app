const API_URL = "http://localhost:8080";
async function fetchProductsMap() {
    const response = await fetch(`${API_URL}/products`);
    const products = await response.json();

    let productMap = {};
    products.forEach(p => {
        productMap[p.name] = p.priceInClouds;
    });

    return productMap;
}

//  Fetch Deals and Return as Map
async function fetchDealsMap() {
    const response = await fetch(`${API_URL}/deals`);
    const deals = await response.json();

    let dealsMap = { "2 for 3": [], "buy 1 get 1 half price": [] };
    deals.forEach(deal => {
        if (deal.type === "TWO_FOR_THREE") {
            dealsMap["2 for 3"].push(...deal.applicableProducts);
        } else if (deal.type === "BUY_ONE_GET_ONE_HALF_PRICE") {
            dealsMap["buy 1 get 1 half price"].push(...deal.applicableProducts);
        }
    });

    return dealsMap;
}
//  Fetch Products
async function fetchProducts() {
    const response = await fetch(`${API_URL}/products`);
    const products = await response.json();
    document.getElementById("productList").innerHTML = products
        .map(p => `<li>${p.name} - ${p.priceInClouds} clouds 
            <button onclick="deleteProduct('${p.name}')">Delete</button></li>`)
        .join("");
}

//  Add Product
async function addProduct() {
    const name = document.getElementById("productName").value;
    const price = document.getElementById("productPrice").value;
    await fetch(`${API_URL}/products`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, priceInClouds: parseInt(price) }),
    });
    fetchProducts();
}

//  Delete Product
async function deleteProduct(name) {
    await fetch(`${API_URL}/products`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name }),
    });
    fetchProducts();
}

//  Fetch Deals
async function fetchDeals() {
    const response = await fetch(`${API_URL}/deals`);
    const deals = await response.json();
    document.getElementById("dealList").innerHTML = deals
        .map(d => `<li>${d.type} - ${d.applicableProducts.join(", ")} 
            <button onclick="deleteDeal(${d.id})">Delete</button></li>`)
        .join("");
}

//  Add Deal
async function addDeal() {
    const type = document.getElementById("dealType").value;
    const applicableProducts = document.getElementById("dealProducts").value.split(",");
    await fetch(`${API_URL}/deals`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ type, applicableProducts }),
    });
    fetchDeals();
}

//  Delete Deal
async function deleteDeal(id) {
    await fetch(`${API_URL}/deals`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ id }),
    });
    fetchDeals();
}

async function fetchBasket() {
    let basketIdField = document.getElementById("basketId");
    let basketId = basketIdField.value.trim();


    if (!basketId) {
        console.warn("No Basket ID entered.");
        return;
    }

    //  Store last used Basket ID
    if (basketId !== localStorage.getItem("lastBasketId")) {
        localStorage.setItem("lastBasketId", basketId);
    }

    const response = await fetch(`${API_URL}/baskets/${basketId}`);
    if (!response.ok) {
        console.error("Failed to fetch basket data.");
        document.getElementById("basketContents").innerHTML = "<li>No items found</li>";
        return;
    }

    const basket = await response.json();

    //  Show scanned items in the UI
    document.getElementById("basketContents").innerHTML = basket.scannedItems
        .map(item => `<li>${item}</li>`)
        .join("");
}

//  Scan Item into Basket
async function scanItems() {
    let basketId = document.getElementById("basketId").value.trim();

    if (!basketId) {
        alert("Please enter a basket ID.");
        return;
    }

    const scanInput = document.getElementById("scanItem");
    const items = scanInput?.value ? scanInput.value.split(",").map(item => item.trim()) : [];

    if (items.length === 0) {
        alert("Please enter at least one item.");
        return;
    }

    await fetch(`${API_URL}/baskets/${basketId}/scan`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(items),
    });

    scanInput.value = "";
    fetchBasket();
}
document.getElementById("basketId").addEventListener("input", (event) => {
    let newBasketId = event.target.value.trim();
    if (!newBasketId) {
        localStorage.removeItem("lastBasketId");
        return;
    }
    localStorage.setItem("lastBasketId", newBasketId);
    fetchBasket();
});
async function clearBasket() {
    let basketId = document.getElementById("basketId").value.trim();

    if (!basketId) {
        alert("Please enter a basket ID to clear.");
        return;
    }

    await fetch(`${API_URL}/baskets/${basketId}/clear`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
    });

    document.getElementById("basketContents").innerHTML = "";
    alert(`Basket ${basketId} cleared.`);
}

async function checkout() {
    let basketId = document.getElementById("checkoutBasketId").value || localStorage.getItem("lastBasketId");

    if (!basketId) {
        alert("Please enter a basket ID to checkout.");
        return;
    }


    const deals = await fetchDealsMap();

    const response = await fetch(`${API_URL}/checkout`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ deals, basketId }),
    });

    const total = await response.text();
    document.getElementById("total").innerText = `Total: ${total}`;
}

// Load Products and Deals on Page Load
window.onload = () => {
    fetchProducts();
    fetchDeals();
    fetchBasket();
};
