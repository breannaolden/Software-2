C195 - Software 2 Project
PURPOSE: A desktop scheduling application that is robust enough to handle timezone manipulation to account for the
global nature of a software company and its clientele.
AUTHOR: Breanna Olden
STUDENT ID: 001532324
EMAIL: bolden2@wgu.edu
VERSION: 1.0
DATE: 07/20/22
IDE: IntelliJ Community Edition 2021.1.3
SDK: JavaFX-SDK-17.0.1
JDK: Java SE 17.0.1
MySQL CONNECTOR DRIVER: 8.0.27

DIRECTIONS FOR RUNNING THE PROGRAM:
1. Upon opening the application, you will be required to log in. Enter username and password, then click "login".
2. Once successfully logged in, you will see the main dashboard, which displays appointments.
3. MAIN DASHBOARD / MAIN APPOINTMENTS PAGE:
    From here, you can add a new appointment, modify or delete an appointment. You can also navigate to
    the Customers page or the Reports page. Additionally, you can logout and close the application by clicking
    the "logout" button in the top right corner of the application.
4. ADD NEW APPOINTMENT PAGE:
    Enter the prompted information in the format suggested by the prompt text. Once all information has been entered,
    click the "Add" button. If any information is entered incorrectly or the hours entered do not fit into the
    business hours, you will receive an error message.
5. MODIFY EXISTING APPOINTMENT PAGE:
    See above for instructions. In order to navigate here, you must first select an appointment in the tableview
    on the main dashboard page. The information regarding this appointment will autopopulate the text fields on
    the modify appointment page.
6. ADD NEW CUSTOMER PAGE:
7. MODIFY EXISTING CUSTOMER PAGE:
8. REPORTS PAGE 1 - Appointments
    This page allows you to view the total number of appointments of a certain type within a selected month. Using the
    drop down combo boxes, select and appointment type. Then, select a month from the month drop down combo box. Click
    "Find" to populate the textfields with the total number of appointments of your selected type of appointment within
    the month you selected.
9. REPORTS PAGE 2 - Contact Schedule
    This page allows you to view all appointments scheduled for each contact individually. Select a contact from the
    dropdown combo box. Click "Find." The textboxes below will autopopulate. For example, say I have 10
    appointments total. Half are scheduled with Li Lee. When I select Li Lee from the drop down combo box and click
    "Find", I will see the number 5.
10. REPORTS PAGE 3 - Average/Total


ADDITIONAL REPORT RUN:
The additional report has two functions:
    1. Calculates the average number of daily appointments by month
    2. Displays the total sum of all appointments scheduled within a given month
       The current database has only two appointments. In a real world scenario, there would be far more
       than two appointments. The number generated will be 0 with the given database, however the number
       would be much more meaningful with a larger number of appointments in the database.
       This page allows you to view the average number of appointments scheduled per day within a selected month.
       Additionally, it also allows you to view the total number of appointments scheduled within a selected month. It is
       important that these numbers are viewed together because they can give context to each of the numbers for analysis.
       In the case of this project, there are only two appointments prepopulated. Therefore, an average daily appointment
       number of 0 is given context by the total number of appointments (2).

In a business setting, these two metrics are useful for understand if certain months are more popular
for having appointments than others. This may influence staffing levels or resource levels provided
in a given month.

LAMBDA EXPRESSIONS
Located in Reports.java and AddAppointment.java
1. Reports.java
Lambda expression functions to condense code by creating a function to pull alert messages. Instead
of needing to type out the entire alert message each time it is  needed within the code, I can type it
once, and then be able to type a one line piece of code utilizing that lambda to pull the correct
alert message.
2. AddAppointment.java
Lambda expression functions to block the choice in date to any date prior to today. This will prevent
addition of appointments in the past that may not have occurred. This was not included in the
modify appointments functionality because one may need to edit previously created appointments which,
by definition, occurred in the past.