# frontend/Dockerfile

# ----------------------------------------
# 1. Build Stage (Creates the optimized assets)
# ----------------------------------------
# Use a Node image to handle installing dependencies and building the project
FROM node:18-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package manifests first to leverage Docker caching (if they haven't changed)
COPY package*.json ./

# Install dependencies (npm ci ensures consistent installs based on lock file)
RUN npm ci

# Copy all source code into the container
COPY . .

# Run the build script, which generates the production-ready static files in the /app/dist folder
RUN npm run build

# ----------------------------------------
# 2. Production Stage (Serves the static assets)
# ----------------------------------------
# Use a lightweight, stable Nginx image to serve the application
FROM nginx:stable-alpine

# Copy the built application files from the 'build' stage into the Nginx public folder
COPY --from=build /app/dist /usr/share/nginx/html

# Optional: handle SPA routing (all requests to index.html)
RUN rm /etc/nginx/conf.d/default.conf

# Copy the built application files from the 'build' stage into the Nginx public folder
COPY nginx.conf /etc/nginx/conf.d/nginx.conf

# Nginx listens on port 80 by default
EXPOSE 80

# The default command for the Nginx image runs the server
CMD ["nginx", "-g", "daemon off;"]

