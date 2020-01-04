### Android Application Fundzilla

### Introduction

FundZilla aims to provide a digital platform to raise money through fundraising campaigns. The platform connects potential donors with fund recipients. Users can choose to create campaigns pitching their ideas by providing a summary of their cause and their fundraising goals. 

Upon logging onto the application, the users see a list of potential campaigns they can choose to donate to along with a short description of the cause and the fundraising goals. The user can then choose to make a donation through the application to the fundraising owner account.

### Technologies used

* •	Android studio: IDE to develop and test android applications
* •	Firebase: Firebase real-time database was used to create database tables and retrieve and store campaign and user related data.Firebase was used as it is easy to setup and tight integration support from android studio is available. 
* •	Google Pay: Google pay integration to retrieve user credit card information.
This removes the need for credit card data handling from the application. In addition, google pay offers the promise of easy integration and the default payment method for most android users
* •	Stripe: The application uses the Stripe connect platform to create customer accounts and route charges to the eventual campaign owner  destination. Stripe also allows platform onboarding with dashboard and user account management tools to target user accounts on the platform and disburse payment amounts to bank accounts

### Design goals/requirements

The technical design goals of the platform/app are as follows:-

* •	Platform should allow users to sign up/login as the basis for identity
* •	Platform should allow user to create a campaign 
* •	Platform should display the user a list of campaigns
* •	Platform should allow user to donate to a campaign of their choice preferably through digital wallet/card service
* •	Platform should allow the campaign owner to redeem funds and transfer the collected funds to a bank account of their choice

### Design Diagram
![Image description](https://github.com/prasadmegha/android-pay-fundzilla/blob/master/1.png)

### App structure
![Image description](https://github.com/prasadmegha/android-pay-fundzilla/blob/master/2.png)


### User login flow

* •	User logins to the application. We look up the user table using email address stored in the user firebase table. If not present, then we create a customer record by recording customer email/password.
* •	Additionally we use PaymentUtils::createStripeCustomer to create a new stripe customer id and store it in the database. 
* •	We then pass along the user id and stripe id to the next activity intent and store for subsequent usage

### Campaign creation flow

* •	Prerequisite: Having the current user id to store the information in the campaign record
* •	User fills out a form indicating the fields of the campaign. 
* •	We then create a campaign record in the firebase table along with the campaign fields. Note that we also save the stripe id in the campaign for the eventual destination routing for the donation

### Payment flow
![Image description](https://github.com/prasadmegha/android-pay-fundzilla/blob/master/3.png)

### UI Mocks

### Login view
![Image description](https://github.com/prasadmegha/android-pay-fundzilla/blob/master/4.png)

### Campaign Lists View
![Image description](https://github.com/prasadmegha/android-pay-fundzilla/blob/master/5.png)

### Campaign page view
![Image description](https://github.com/prasadmegha/android-pay-fundzilla/blob/master/6.png)

### Create campaign view
![Image description](https://github.com/prasadmegha/android-pay-fundzilla/blob/master/7.png)
