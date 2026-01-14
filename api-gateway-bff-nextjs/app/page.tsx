/**
 * Main Dashboard Page
 * Fetches data from BFF endpoint through API Gateway
 *
 * Now the frontend makes requests through the Gateway:
 * Frontend (localhost:8888) → Gateway (localhost:8888) → BFF endpoint
 */

import { DashboardData } from "@/types/dashboard";

// ============================================================
// UPDATED: Now using Gateway URL instead of direct BFF URL
// Since frontend is served from localhost:8888, we can use relative URL
// ============================================================
const BFF_API_URL = 'http://localhost:8888/bff/dashboard';

/**
 * Fetch dashboard data from BFF through Gateway
 */
async function getDashboardData(): Promise<DashboardData> {
  try {
    // When frontend is accessed via localhost:9999, this relative URL
    // will automatically go through the Gateway
    const response = await fetch(BFF_API_URL, {
      cache: "no-store",
    });

    if (!response.ok) {
      throw new Error("Failed to fetch dashboard data");
    }

    return response.json();
  } catch (error) {
    console.error("Error fetching dashboard data:", error);
    return { products: [], categories: [] };
  }
}

/**
 * Dashboard Page Component
 */
export default async function DashboardPage() {
  const data = await getDashboardData();

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <h1 style={{ fontSize: "32px", marginBottom: "30px" }}>
        📊 Microservices Dashboard
      </h1>

      {/* Gateway Info Banner */}
      <div
        style={{
          padding: "15px",
          backgroundColor: "#e0f2fe",
          borderRadius: "8px",
          marginBottom: "30px",
          border: "2px solid #0284c7",
        }}
      >
        <h3 style={{ margin: "0 0 10px 0", color: "#0c4a6e" }}>
          🌐 Accessing via API Gateway
        </h3>
        <p style={{ margin: 0, fontSize: "14px", color: "#075985" }}>
          <strong>Current URL:</strong>{" "}
          {typeof window !== "undefined"
            ? window.location.origin
            : "http://localhost:8888"}
          <br />
          <strong>API Endpoint:</strong> {BFF_API_URL}
          <br />
          <strong>Architecture:</strong> Gateway routes to BFF, BFF aggregates
          microservices
        </p>
      </div>

      {/* Categories Section */}
      <section style={{ marginBottom: "40px" }}>
        <h2
          style={{
            fontSize: "24px",
            marginBottom: "15px",
            borderBottom: "2px solid #333",
            paddingBottom: "5px",
          }}
        >
          📁 Categories ({data.categories.length})
        </h2>

        {data.categories.length === 0 ? (
          <p
            style={{
              padding: "20px",
              backgroundColor: "#fef2f2",
              borderRadius: "8px",
            }}
          >
            ⚠️ No categories available
          </p>
        ) : (
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))",
              gap: "15px",
            }}
          >
            {data.categories.map((category) => (
              <div
                key={category.id}
                style={{
                  border: "1px solid #ddd",
                  padding: "15px",
                  borderRadius: "8px",
                  backgroundColor: "#f9f9f9",
                }}
              >
                <h3 style={{ margin: "0 0 10px 0", fontSize: "18px" }}>
                  {category.name}
                </h3>
                <p style={{ margin: 0, color: "#666", fontSize: "14px" }}>
                  {category.description}
                </p>
                <p
                  style={{
                    margin: "10px 0 0 0",
                    fontSize: "12px",
                    color: "#999",
                  }}
                >
                  ID: {category.id}
                </p>
              </div>
            ))}
          </div>
        )}
      </section>

      {/* Products Section */}
      <section>
        <h2
          style={{
            fontSize: "24px",
            marginBottom: "15px",
            borderBottom: "2px solid #333",
            paddingBottom: "5px",
          }}
        >
          🛍️ Products ({data.products.length})
        </h2>

        {data.products.length === 0 ? (
          <p
            style={{
              padding: "20px",
              backgroundColor: "#fef2f2",
              borderRadius: "8px",
            }}
          >
            ⚠️ No products available
          </p>
        ) : (
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))",
              gap: "20px",
            }}
          >
            {data.products.map((product) => (
              <div
                key={product.id}
                style={{
                  border: "1px solid #ddd",
                  borderRadius: "8px",
                  overflow: "hidden",
                  backgroundColor: "#fff",
                  boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                }}
              >
                <img
                  src={product.imageUrl}
                  alt={product.name}
                  style={{
                    width: "100%",
                    height: "200px",
                    objectFit: "cover",
                  }}
                />
                <div style={{ padding: "15px" }}>
                  <h3 style={{ margin: "0 0 10px 0", fontSize: "18px" }}>
                    {product.name}
                  </h3>
                  <p
                    style={{
                      margin: "0 0 5px 0",
                      fontSize: "20px",
                      fontWeight: "bold",
                      color: "#2563eb",
                    }}
                  >
                    ${product.price.toFixed(2)}
                  </p>
                  <p style={{ margin: 0, fontSize: "12px", color: "#888" }}>
                    Category ID: {product.categoryId}
                  </p>
                </div>
              </div>
            ))}
          </div>
        )}
      </section>

      {/* Footer Info */}
      <footer
        style={{
          marginTop: "40px",
          padding: "20px",
          backgroundColor: "#f0f0f0",
          borderRadius: "8px",
        }}
      >
        <h3 style={{ margin: "0 0 10px 0" }}>ℹ️ Architecture Flow</h3>
        <ul style={{ margin: 0, paddingLeft: "20px", lineHeight: "1.8" }}>
          <li>
            ✅ <strong>Frontend Access:</strong>{" "}
            <code>http://localhost:8888/</code> (via Gateway)
          </li>
          <li>
            ✅ <strong>Direct Access:</strong>{" "}
            <code>http://localhost:3000/</code> (Next.js server)
          </li>
          <li>
            ✅ <strong>BFF Endpoint:</strong> Gateway routes{" "}
            <code>/bff/dashboard</code> to BFF Controller
          </li>
          <li>
            ✅ <strong>Microservices:</strong> BFF aggregates Product Service
            (8081) + Category Service (8082)
          </li>
          <li>
            ✅ <strong>Single Entry Point:</strong> All requests go through
            Gateway (port 8888)
          </li>
        </ul>
      </footer>
    </div>
  );
}