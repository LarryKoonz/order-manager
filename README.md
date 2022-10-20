# Restaurant API
REST API for managing online orders of a restaurant.

## Table of Contents
[Requirements and Dependencies](#requirements_and_dependencies)<br />
[How to Set Up and Run the Project](#how_to_set_up_and_run_the_project)<br />
[How to Use](#how_to_use)<br />
[Error Codes](#error_codes)

## Requirements and Dependencies<a name="requirements_and_dependencies"/>
- Java >= 11
- Spring Boot 2.7.4
- Spring Web
- Spring Data JPA
- PostgreSQL Driver

## How to Set Up and Run the Project<a name="how_to_set_up_and_run_the_project"/>
Before setting up and running the project make sure that you have [Docker](https://docs.docker.com/engine/install/) and [Maven](https://maven.apache.org/) on your machine.<br />
There are two options:
1. Download the docker image from [here](https://hub.docker.com/) and follow the instructions [here](https://docs.docker.com/engine/reference/commandline/run/) on running the image that you have downloaded.
2. Run the following commands in your terminal:
```bash
git clone https://github.com/LarryKoonz/order-manager.git
cd order-manager
chmod +x mvnw
mvn -N wrapper:wrapper
sudo docker build -t order-manager .
sudo docker compose up
```
Now you can perform requests to consume the API at http://localhost:8080/api/v1/order with the endpoints that are described [here](#how_to_use).<br />
After exiting the application run the followinng command:
```bash
sudo docker compose down
```

## How to Use<a name="how_to_use"/>
### Get order
- **GET** api/v1/order/{id}
- **Accept**: application/JSON
- **Content-Type**: application/JSON
- **Code**: 200 OK

### Save new order
- **POST** api/v1/order
- **Accept**: application/JSON
- **Content-Type**: application/JSON
- **Code**: 201 CREATED

### Get all orders from the last day
- **GET** api/v1/order/lastDay
- **Accept**: application/JSON
- **Content-Type**: application/JSON
- **Code**: 200 OK

### Get all orders from the last week
- **GET** api/v1/order/lastWeek
- **Accept**: application/JSON
- **Content-Type**: application/JSON
- **Code**: 200 OK

### Get all orders from the last month
- **GET** api/v1/order/lastMonth
- **Accept**: application/JSON
- **Content-Type**: application/JSON
- **Code**: 200 OK

### Change order (that was scheduled 15 minutes ago)
- **PUT** api/v1/order/{id}
- **Accept**: application/JSON
- **Content-Type**: application/JSON
- **Code**: 204 NO CONTENT


## Error Codes<a name="error_codes"/>
| Code | Description    | Reason    |
| :---:   | :---: | :---: |
| 400 | Bad Request   | Missing parameters or invalid parameters.   |
| 404 | Not Found   | Value was not found in data base.   |
