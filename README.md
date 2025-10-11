
# OpenAI-OCI Gateway 🚀

[![License: Apache-2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)  
[![Maven Central](https://img.shields.io/maven-central/v/com.example/openai-oci-gateway.svg?label=Maven%20Central)](https://search.maven.org/)  
[![GitHub stars](https://img.shields.io/github/stars/StykMartin/openai-oci-gateway.svg)](https://github.com/StykMartin/openai-oci-gateway/stargazers)  

---

## 📌 What is it?

**openai-oci-gateway** is a lightweight API gateway that makes **OCI Generative AI** services fully **OpenAI-compatible**.  
It allows you to switch your backend to Oracle Cloud with *zero changes* in your existing OpenAI client code—just change the base URL.

| Feature | Benefit |
|---|---|
| **Drop-in compatibility** | Use your existing OpenAI SDKs, clients, and tooling unchanged. |
| **OCI Gen AI backend** | Takes advantage of Oracle’s infrastructure, cost model, compliance, etc. |
| **Minimal footprint** | Lightweight, easy to deploy, and low overhead. |
| **Configurable** | Flexible routing, authentication, and request transformations. |

---

## 🧰 Supported Endpoints

The gateway supports common OpenAI-style endpoints (mapped to OCI equivalents):

- `POST /v1/chat/completions`
- `POST /v1/completions`
- `POST /v1/embeddings`
- `POST /v1/models`
- `GET  /v1/models`  
- *(and more — please refer to the full spec in docs)*

---

## ⚙️ How it Works

1. Your OpenAI client (Python, Node.js, Java, etc.) sends a request to the gateway instead of directly to OpenAI.
2. The gateway translates the OpenAI-style request into the corresponding OCI Generative AI API format.
3. It sends the transformed request to OCI Gen AI, receives the response, and converts it back into OpenAI-style JSON.
4. Your client receives an OpenAI-style response with **no code change on your side**.

---

## 🚀 Quick Start

### Prerequisites

- Java 17+  
- OCI account with access to Generative AI services  
- Credentials (API key / auth) for OCI

### Installation / Deployment

You can run the gateway in various ways:

- As a **standalone JAR**  
- As a container (Docker)  
- In a Kubernetes / serverless environment  

Example (with JAR):

```bash
java -jar openai-oci-gateway.jar \
  --oci.authKey="YOUR_OCI_KEY" \
  --oci.region="us-ashburn-1" \
  --oci.genai.endpoint="https://genai.oraclecloud.com" \
  --server.port=8080
````

### Usage

Point your OpenAI-compatible client’s base URL to the gateway:

```python
import openai

openai.api_base = "http://your-gateway-host:8080/v1"
openai.api_key = "ANY"  # (if required by gateway)
completion = openai.ChatCompletion.create(
    model="gpt-4o", messages=[{"role": "user", "content": "Hello"}]
)
print(completion.choices[0].message.content)
```

That’s it — your client behaves exactly as before.

---

## 📚 Configuration

* **Authentication / Authorization** — support API keys, tokens, or OCI IAM roles
* **Request/response mapping rules** — e.g. transform or filter fields
* **Model whitelist / blacklists**
* **Timeouts, retries, rate limiting**
* **Logging, metrics, observability**

(The full set of configuration properties is documented in the [CONFIGURATION.md] file.)

---

## 🧩 Architecture Overview

```
[Your Client (OpenAI SDK)] → → → → → → → → → → → → → → → → → → → → → → →
        |
        | → Gateway (this project)  
        |     • Parses OpenAI-style request  
        |     • Transforms into OCI Gen AI API  
        |     • Sends to OCI  
        |     • Receives response, transforms back  
        |
        → → → → → → → → → → → → → → → → → → → → → → → →
```

Key components:

* **Request handler / router**
* **Transformation layer**
* **OCI client adapter**
* **Middleware (logging, metrics, auth, retries)**

---

## ✅ When & Why Use This

* You want to host generative AI workloads on **OCI** for cost, security, compliance, or proximity reasons
* You already have code built for OpenAI and don’t want to rewrite or re-adapt it
* You need abstraction between your client and the underlying generative API
* You want flexibility to swap backend in the future (e.g. switch providers)

---

## 🧑‍💻 Contributing

Contributions, issues, and pull requests are welcome!
Please see [CONTRIBUTING.md] for guidelines and code of conduct.

---

## 🧾 License & Acknowledgments

This project is licensed under the **Apache 2.0** License — see the [LICENSE](LICENSE) file for details.

---


**Enjoy building generative AI applications on OCI — with zero code changes!**






## Contributors
<img src="https://contributors-img.web.app/image?repo=StykMartin/openai-oci-gateway"/>

## Stargazers
<img src="https://reporoster.com/stars/dark/StykMartin/openai-oci-gateway"/>

## Forkers
<img src="https://reporoster.com/forks/dark/StykMartin/openai-oci-gateway"/>
