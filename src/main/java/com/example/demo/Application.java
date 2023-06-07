package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository) {
        return args -> {
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            int age = faker.number().numberBetween(10, 100);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    age);
            StudentIdCard studentIdCard = new StudentIdCard(
                    "123456789",
                    student);

            student.addBook(new Book(
                    faker.book().title(),
                    LocalDateTime.now()
            ));

            student.addBook(new Book(
                    faker.book().title(),
                    LocalDateTime.now()
            ));

            student.addBook(new Book(
                    faker.book().title(),
                    LocalDateTime.now()
            ));

            student.setStudentIdCard(studentIdCard);

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 1L),
                    student,
                    new Course(faker.educator().course(), faker.educator().campus()),
                    LocalDateTime.now()
            ));

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 2L),
                    student,
                    new Course(faker.educator().course(), faker.educator().campus()),
                    LocalDateTime.now().minusDays(18)
            ));

//            student.enrolToCourse(
//                    new Course(faker.educator().course(), faker.educator().campus()));
//
//            student.adenrolToCourse(
//                    new Course(faker.educator().course(), faker.educator().campus()));

            studentRepository.save(student);

//            studentRepository.findById(1L)
//                    .ifPresent(System.out::println);

            studentRepository.findById(1L)
                    .ifPresent(s -> {
                        System.out.println("fetch books lazy ...");
                        List<Book> books = student.getBooks();
                        books.forEach(book -> {
                            System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
                        });
                    });

//            studentIdCardRepository.findById(1L)
//                    .ifPresent(System.out::println);

//            studentRepository.deleteById(1L);

        };
    }

    private static void sorting(StudentRepository studentRepository) {
        Sort sort = Sort.by("firstName").ascending()
                .and(Sort.by("age").descending());

        studentRepository.findAll(sort)
                .forEach(student -> System.out.println(student.getFirstName() + " " + student.getAge()));
    }

    private static void generateRandomStudents(StudentRepository studentRepository) {
        Faker faker = new Faker();

        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            int age = faker.number().numberBetween(10, 100);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    age);
            studentRepository.save(student);
        }
    }

}
