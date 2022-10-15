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


## How to Use<a name="how_to_use"/>
With the web server and the Postgresql started and running, perform the following requests to consume the API:
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
