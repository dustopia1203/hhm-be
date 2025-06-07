#!/bin/bash

# URL của Elasticsearch
ELASTICSEARCH_URL="http://localhost:9200"

# Đường dẫn đến file template JSON
PRODUCTS_TEMPLATE_FILE="products-template.json"
USER_BEHAVIOR_TEMPLATE_FILE="user-behaviors-template.json"


# Tên của template
PRODUCTS_TEMPLATE_NAME="products_template"
USER_BEHAVIOR_TEMPLATE_NAME="user_behaviors_template"


# Kiểm tra Elasticsearch có đang chạy không
echo "Checking if Elasticsearch is running..."
if curl -s "$ELASTICSEARCH_URL" > /dev/null; then
  echo "Elasticsearch is running. Proceeding to load templates..."
else
  echo "Elasticsearch is not running. Please start Elasticsearch and try again."
  exit 1
fi

# Kiểm tra xem products index đã tồn tại chưa
  echo "Loading products template into Elasticsearch..."
  curl -X PUT "$ELASTICSEARCH_URL/_index_template/$PRODUCTS_TEMPLATE_NAME" \
  -H "Content-Type: application/json" \
  -d @"$PRODUCTS_TEMPLATE_FILE"
  echo "Products template loaded successfully!"


# Kiểm tra xem user_behaviors index đã tồn tại chưa
  echo "Loading user behavior template into Elasticsearch..."
  curl -X PUT "$ELASTICSEARCH_URL/_index_template/$USER_BEHAVIOR_TEMPLATE_NAME" \
  -H "Content-Type: application/json" \
  -d @"$USER_BEHAVIOR_TEMPLATE_FILE"
  echo "User behavior template loaded successfully!"


echo "Template loading process completed!"