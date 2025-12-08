# Multi-stage build for React Frontend
# Stage 1: Build the React app
FROM node:18-alpine AS build
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies (use npm install to handle lock file mismatches)
# We need dev dependencies for building React app
RUN npm install

# Copy source code
COPY . .

# Build the React app with environment variable for API URL
ARG REACT_APP_API_URL
ENV REACT_APP_API_URL=${REACT_APP_API_URL}
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine

# Copy built app from build stage
COPY --from=build /app/build /usr/share/nginx/html

# Copy nginx config template
# Nginx's built-in entrypoint will automatically process templates in /etc/nginx/templates/
# and output them to /etc/nginx/conf.d/ using envsubst
COPY nginx.conf.template /etc/nginx/templates/default.conf.template

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Nginx's default entrypoint will handle template substitution automatically
