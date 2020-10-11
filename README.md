# ProductSite
This project involves services for adding products and listing products by categories for a hypothetical e-commerce website.

It supplies the following 5 services as REST API. It also contains unit and integration tests for these services. 
It uses in-memory H2 DB for storing data. In /src/main/resources folder, you can reach SQL queries that populates the DB.
Sample usage for the services is alse shared below.

1) List Categories
Sample Request: GET http://localhost:8080/categories
Sample Response: 
[{"id":1,"name":"Ev Eşyası"},{"id":2,"name":"Elbise"},{"id":3,"name":"Aksesuar"}]

2) Find Category By Category Id
Sample Request: GET http://localhost:8080/categories/3
Sample Response:
{"id":3,"name":"Aksesuar"}

3) Add Product
Sample Request: POST http://localhost:8080/addProduct
{
  "name": "Kırmızı küpe",
  "explanation" : "Uzun, M beden, mavi",
  "category" : {
    "id" : 3
  },
  "seller"   : {
    "id" : 2
  },
  "price"       : 250
}
Sample Response:
1

4) Find Products By Category Name
Sample Request: GET http://localhost:8080/categories/Elbise/products
Sample Response :
[{"id":4,"name":"Mavi Elbise","explanation":"Uzun, M beden, mavi","price":250.0,"seller":{"id":2,"username":"ozis"},"category":{"id":2,"name":"Elbise"}},
{"id":5,"name":"Gömlek Elbise","explanation":"Siyah Kuşaklı, S","price":250.0,"seller":{"id":1,"username":"busra"},"category":{"id":2,"name":"Elbise"}}]

5) Find Products By Category Id
Sample Request: GET http://localhost:8080/categoriesById/2/products
Sample Response:
[{"id":4,"name":"Mavi Elbise","explanation":"Uzun, M beden, mavi","price":250.0,"seller":{"id":2,"username":"ozis"},"category":{"id":2,"name":"Elbise"}},
{"id":5,"name":"Gömlek Elbise","explanation":"Siyah Kuşaklı, S","price":250.0,"seller":{"id":1,"username":"busra"},"category":{"id":2,"name":"Elbise"}}]
