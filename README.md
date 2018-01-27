# Updater

A program developed for college as proof of knowledge. **Do not use it in production** nor as an example of good SOLID principle without previous checking the code.

## Description of program

The program is shipped with two folders: "programs" and "serverprograms". The aim of the program is to check the list of programs installed on the client computer (using JNDI) and upgrade them with programs from the server. Upon opening, the program first checks local programs and stores them in the serialized object to "programs.dat" file in the root of the project. All found programs are shown as list to the user. By clicking on the *update* button, two pools of threads are fired up. One pool of threads checks the program version on the server (using sockets) and compare it to client version. If the program is required to update, a second pool of threads fires up RMI to the server and downloads the new program. The downloaded program is stored with the prefix "Updated" in the same folder as other client programs and does not overwrite old programs as this is just proof of concept. After all programs are downloaded, the program shows a message box.

The server on the other hand, stores program versions in an XML format called "data.xml". After some time (defined in code) the server save statistics in "data2.xml" file. Those statistics show all connected clients (and their program versions) and the program versions of all programs on the server.

![alt text](https://github.com/SanjinKurelic/UpdaterJavaFX/blob/master/resources/img.PNG "Program GUI")

## Technologies

This program provides examples of many Java technologies such as:

+ **JavaFX** - for showing GUI of the application
+ **RMI** - for downloading programs from the server
+ **Socket** - for checking new versions of program from a server
+ **SAXParser** - for storing data about programs on server in XML file format
+ **JNDI** - for getting programs list
+ **Reflection** - for building JSON request for future usage
+ **Serialization** - for storing data about program versions on the client computer
+ **Threads** - for parallel communication with the server

## Notice

Program generates this files (which you should delete before second usage):

+ "programs.dat" in the root directory - for storing serialized data about client programs information
+ "Updated \*" group of files in "programs" directory - programs that are updated
+ "data2.xml" in "serverprograms" directory - stored info about server and client programs

## Requirements

+ *fscontext* - for JNDI (provieded in the libs folder)