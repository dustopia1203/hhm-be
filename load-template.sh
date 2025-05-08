#!/bin/bash

# URL của Elasticsearch
ELASTICSEARCH_URL="http://localhost:9200"

# Đường dẫn đến file template JSON
TEMPLATE_FILE="elasticsearch-index-template.json"

# Tên của template
TEMPLATE_NAME="products_template"

# Kiểm tra Elasticsearch có đang chạy không
echo "Checking if Elasticsearch is running..."
if curl -s "$ELASTICSEARCH_URL" > /dev/null; then
  echo "Elasticsearch is running. Proceeding to load template..."
else
  echo "Elasticsearch is not running. Please start Elasticsearch and try again."
  exit 1
fi

# Tải template vào Elasticsearch
echo "Loading template into Elasticsearch..."
curl -X PUT "$ELASTICSEARCH_URL/_index_template/$TEMPLATE_NAME" \
-H "Content-Type: application/json" \
-d @"$TEMPLATE_FILE"

echo "Template loaded successfully!"