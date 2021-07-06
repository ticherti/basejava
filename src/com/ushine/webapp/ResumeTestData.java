package com.ushine.webapp;

import com.ushine.webapp.model.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResumeTestData {

    public static Resume getResume(String fullname, String uuid) {
        Resume resume = new Resume(fullname, uuid);
        fillContacts(resume);
        fillSections(resume);
        return resume;
    }

    public static void main(String[] args) {
        Resume test = new Resume("Григорий Кислин");

        fillContacts(test);
        fillSections(test);

        System.out.println(test.getFullName());
        printContacts(test);
        for (Map.Entry<SectionType, AbstractSection> entry : test.getSections().entrySet()
        ) {
            System.out.println();
            System.out.println(entry.getKey().getTitle());
            System.out.println();
            System.out.println(entry.getValue().toString());
        }
    }

    public static void fillContacts(Resume resume) {
        resume.getContacts().put(ContactType.PHONENUMBER, new Link("+7(921) 855-0482", ""));
        resume.getContacts().put(ContactType.SKYPE, new Link("grigory.kislin", "skype:grigory.kislin"));
        resume.getContacts().put(ContactType.EMAIL, new Link("gkislin@yandex.ru", "mailto:gkislin@yandex.ru"));
        resume.getContacts().put(ContactType.LINKEDIN, new Link("https://www.linkedin.com/in/gkislin"));
        resume.getContacts().put(ContactType.GITHUB, new Link("https://github.com/gkislin"));
        resume.getContacts().put(ContactType.STACKOVERFLOW, new Link("https://stackoverflow.com/users/548473"));
        resume.getContacts().put(ContactType.HOMEPAGE, new Link("http://gkislin.ru/"));
    }

    public static void fillSections(Resume resume) {

        resume.getSections().put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.getSections().put(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        resume.getSections().put(SectionType.ACHIEVEMENT, createListForAchievements());
        resume.getSections().put(SectionType.QUALIFICATIONS, createListForQualifications());

        resume.getSections().put(SectionType.EXPERIENCE, createListForExperience());
        resume.getSections().put(SectionType.EDUCATION, createListforEducation());

    }

    private static ListSection createListForAchievements() {
        List<String> lines = new ArrayList<>();
        lines.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        lines.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        lines.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        lines.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        lines.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
        lines.add("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        return new ListSection(lines);
    }

    private static ListSection createListForQualifications() {
        List<String> lines = new ArrayList<>();
        lines.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        lines.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        lines.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,");
        lines.add("MySQL, SQLite, MS SQL, HSQLDB");
        lines.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy,");

        lines.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts,");
        lines.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).");
        lines.add("Python: Django.");
        lines.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        lines.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");

        lines.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.");
        lines.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix,");
        lines.add("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer.");
        lines.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования");
        lines.add("Родной русский, английский \"upper intermediate\"");

        return new ListSection(lines);
    }

    private static OrganizationSection createListForExperience() {

        List<Position> positions = new ArrayList<>();
        positions.add(new Position(YearMonth.parse("2013-10"), null, "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок."));

        Organization newOrg = new Organization("Java Online Projects", "", positions);

        List<Organization> list = new ArrayList<>();
        list.add(newOrg);

        return new OrganizationSection(list);
    }

    private static OrganizationSection createListforEducation() {

        List<Position> positions = new ArrayList<>();
        positions.add(new Position(YearMonth.parse("1987-09"), YearMonth.parse("1993-07"), "Инженер (программист Fortran, C)", null));
        positions.add(new Position(YearMonth.parse("1993-09"), YearMonth.parse("1996-07"), "Аспирантура (программист С, С++)", null));

        Organization newOrg = new Organization("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "http://www.ifmo.ru/", positions);

        List<Organization> list = new ArrayList<>();
        list.add(newOrg);

        return new OrganizationSection(list);
    }

    public static void printContacts(Resume resume) {
        for (Map.Entry<ContactType, Link> entry : resume.getContacts().entrySet()
        ) {
            System.out.println(entry.getKey().getTitle());
            System.out.println(entry.getValue().toString());
        }
    }
}
