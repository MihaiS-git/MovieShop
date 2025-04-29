# Build stage
FROM node:22 AS build

WORKDIR /app

COPY package*.json vite.config.ts tsconfig*.json ./
COPY index.html ./
COPY src ./src
COPY public ./public

RUN npm install
RUN npm run build


# Run stage
FROM nginx:stable-alpine

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
