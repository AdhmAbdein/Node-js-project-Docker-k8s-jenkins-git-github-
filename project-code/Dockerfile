FROM node:16-alpine
WORKDIR /app

COPY ./project-code  /app

RUN npm install
RUN npm install mocha supertest chai --save-dev

CMD [ "npm", "start"]

EXPOSE 3000 
#same port inside app.js
