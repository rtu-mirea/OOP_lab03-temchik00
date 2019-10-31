package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class PizzaSystem {
    ArrayList<Client> users;
    Admin admin = new Admin();
    Client activeUser;
    MyMap myMap;
    public PizzaSystem(){
        users = new ArrayList<Client>(0);
        activeUser = null;
        myMap = new MyMap();
    }

    public void userMenu(){
        String options = "1) Вывести информацию о себе\n2) Изменить имя,\n3) Изменить пароль," +
                "\n4) Изменить местоположение,\n5) Найти кратчайшее растояние от указанной точки," +
                "\n0) Выйти.\nВведите команду: ";
        Scanner in = new Scanner(System.in);
        int com = -1;
        while(com != 0) {
            System.out.print(options);
            com = in.nextInt();
            String log, pass, name, place;
            boolean found;
            switch (com) {
                case 0:
                    System.out.println("Выход в главное меню...2");
                    break;
                case 1:
                    System.out.println("Имя: " + activeUser.getName() + " местоположение: "
                            + activeUser.getPlace() + " логин: " + activeUser.getLogin());
                    break;
                case 2:
                    System.out.print("Введите новое имя: ");
                    name = in.next();
                    activeUser.setName(name);
                    break;
                case 3:
                    System.out.print("Введите новый пароль: ");
                    pass = in.next();
                    activeUser.setPassword(pass);
                    break;
                case 4:
                    System.out.print("Введите новое местоположение: ");
                    place = in.next();
                    activeUser.setPlace(place);
                    break;
                case 5:
                    System.out.print("Введите местоположение, откуда доставлять: ");
                    place = in.next();
                    ArrayList<String> path = myMap.generateShortWay(activeUser.getPlace(), place);
                    if(path.size() == 0)
                        System.out.println("Пути не существует");
                    else {
                        System.out.print("Кратчайший путь: ");
                        for(int i = 0; i < path.size() - 1; ++i)
                            System.out.print(path.get(i) + "->");
                        System.out.println(path.get(path.size() - 1));
                    }
                    break;
                default:
                    System.out.println("Команда не распознана, повторите ввод");
                    break;
            }
        }
    }

    public void adminMenu(){
        String options = "1) Изменить пароль,\n2) Добавить дорогу,\n" +
                "3) Найти кратчайший путь между двумя точками,\n0) Выйти.\nВведите команду: ";
        Scanner in = new Scanner(System.in);
        int com = -1;
        while(com != 0) {
            System.out.print(options);
            com = in.nextInt();
            String pass, place1, place2;
            int weight;
            switch (com) {
                case 0:
                    System.out.println("Выход в главное меню...");
                    break;
                case 1:
                    System.out.print("Введите новый пароль: ");
                    pass = in.next();
                    admin.setPassword(pass);
                    break;
                case 2:
                    System.out.println("Введите первый адрес");
                    place1 = in.next();
                    System.out.println("Введите второй адрес");
                    place2 = in.next();
                    System.out.println("Введите время, затрачиваемое на перемещение: ");
                    weight = in.nextInt();
                    myMap.addPath(place1, place2, weight);
                    System.out.println("Путь добавлен успешно");
                    break;
                case 3:
                    System.out.println("Введите первый адрес");
                    place1 = in.next();
                    System.out.println("Введите второй адрес");
                    place2 = in.next();
                    ArrayList<String> path = myMap.generateShortWay(place1, place2);
                    if(path == null)
                        System.out.println("Пути не существует");
                    else {
                        System.out.print("Кратчайший путь: ");
                        for(int i = 0; i < path.size() - 1; ++i)
                            System.out.print(path.get(i) + "->");
                        System.out.println(path.get(path.size() - 1));
                    }
                    break;
                default:
                    System.out.println("Команда не распознана, повторите ввод");
                    break;
            }
        }
    }

    public void mainMenu(){
        String options = "1) Войти,\n2) Зарегистрироваться,\n0) Выйти.\nВведите команду: ";
        Scanner in = new Scanner(System.in);
        int com = -1;
        while(com != 0){
            System.out.print(options);
            com = in.nextInt();
            String log, pass, name, place;
            boolean found;
            switch(com){
                case 0:
                    System.out.println("Завершение работы...");
                    break;
                case 1:
                    System.out.print("Введите логин: ");
                    log = in.next();
                    System.out.print("Введите пароль: ");
                    pass = in.next();
                    found = false;
                    if(admin.enter(log, pass)){
                        activeUser = null;
                        found = true;
                        adminMenu();
                    }
                    if(!found)
                        for(Client user: users){
                            if(user.enter(log, pass)){
                                activeUser = user;
                                found = true;
                                userMenu();
                            }
                        }
                    if(!found){
                        System.out.println("Логин или пароль введены неверно.");
                    }
                    break;
                case 2:
                    System.out.print("Введите логин: ");
                    log = in.next();
                    found = false;
                    if(admin.getLogin().compareTo(log) == 0){
                        System.out.println("Данный логин уже занят");
                        found = true;
                    }
                    if(!found)
                        for(Client user: users){
                            if(user.getLogin().compareTo(log) == 0){
                                System.out.println("Данный логин уже занят");
                                found = true;
                            }
                        }
                    if(!found){
                        System.out.print("Введите пароль: ");
                        pass = in.next();
                        System.out.print("Введите своё имя: ");
                        name = in.next();
                        System.out.print("Введите своё местоположение: ");
                        place = in.next();
                        users.add(new Client(log, pass, name, place));
                        System.out.println("Аккаунт создан");
                    }
                    break;
                default:
                    System.out.println("Команда не распознана, повторите ввод");
                    break;
            }
        }
    }
}
