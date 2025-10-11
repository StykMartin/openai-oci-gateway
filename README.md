# OpenAI API Gateway for Oracle Cloud Infrastructure (OCI) Generative AI

This project provides an **API gateway** that makes OCI Generative AI services
compatible with the **OpenAI API format**.

With this gateway, you can use your **existing OpenAI applications, SDKs, or tools**
with OCI GenAI by simply changing the API base URL - **no code changes required**.

## Authentication

The gateway accepts **OpenAI-formatted API keys** for compatibility with existing OpenAI client libraries:

- **Legacy format**: `sk-` followed by exactly 48 alphanumeric characters
- **New formats**: `sk-proj-` or `sk-svcacct-` followed by 40-200 alphanumeric characters, underscores, or dashes
