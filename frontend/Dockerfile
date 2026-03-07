# Stage 1: Build React
FROM node:18-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: NGINX
FROM nginx:1.25-alpine

COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

# Override entrypoint to skip docker-entrypoint.sh
ENTRYPOINT ["nginx"]
CMD ["-g", "daemon off;"]
