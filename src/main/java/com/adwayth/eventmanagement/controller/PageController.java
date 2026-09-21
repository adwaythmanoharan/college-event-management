package com.adwayth.eventmanagement.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.adwayth.eventmanagement.entity.Admin;
import com.adwayth.eventmanagement.entity.Event;
import com.adwayth.eventmanagement.entity.Notification;
import com.adwayth.eventmanagement.repository.AdminRepository;
import com.adwayth.eventmanagement.repository.EventRepository;
import com.adwayth.eventmanagement.repository.NotificationRepository;
import com.adwayth.eventmanagement.repository.RegistrationRepository;
import com.adwayth.eventmanagement.repository.StudentRepository;
import com.adwayth.eventmanagement.repository.SubEventRepository;

import jakarta.servlet.http.HttpSession;

import com.adwayth.eventmanagement.entity.Registration;
import com.adwayth.eventmanagement.entity.Student;
import com.adwayth.eventmanagement.entity.SubEvent;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.Cell;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;



@Controller
public class PageController {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final SubEventRepository subEventRepository;
    private final AdminRepository adminRepository;
    private final NotificationRepository notificationRepository;
    

    public PageController(EventRepository eventRepository,RegistrationRepository registrationRepository,StudentRepository studentRepository,SubEventRepository subEventRepository,AdminRepository adminRepository,NotificationRepository notificationRepository
)
    {
        this.eventRepository=eventRepository;
        this.registrationRepository=registrationRepository;
        this.studentRepository=studentRepository;
        this.subEventRepository=subEventRepository;
        this.adminRepository = adminRepository;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/student-register")
public String studentRegisterPage() {

    return "student-register";
}

@PostMapping("/student-register")
public String studentRegister(
        @RequestParam int studentId,
        @RequestParam String studentName,
        @RequestParam String password,
        @RequestParam String confirmPassword,
        Model model) {

    // Check whether student exists
    Student student =
            studentRepository.findById(studentId).orElse(null);

    if (student == null) {

        model.addAttribute(
                "error",
                "Student ID does not exist."
        );

        return "student-register";
    }


    // Check whether account already exists
    if (student.getPassword() != null
            && !student.getPassword().isEmpty()) {

        model.addAttribute(
                "error",
                "An account already exists for this Student ID."
        );

        return "student-register";
    }


    // Check student name
    if (studentName == null
            || studentName.trim().isEmpty()) {

        model.addAttribute(
                "error",
                "Student name cannot be empty."
        );

        return "student-register";
    }


    // Check password
    if (password == null
            || password.trim().isEmpty()) {

        model.addAttribute(
                "error",
                "Password cannot be empty."
        );

        return "student-register";
    }


    // Check password confirmation
    if (!password.equals(confirmPassword)) {

        model.addAttribute(
                "error",
                "Passwords do not match."
        );

        return "student-register";
    }


    // Save student name
    student.setStudentName(studentName.trim());


    // Save password
    student.setPassword(password);


    // Update student record
    studentRepository.save(student);


    // Success
    model.addAttribute(
            "success",
            "Account created successfully. You can now login."
    );

    return "student-register";
}

     @PostMapping ("/login")
    public String login(@RequestParam int studentId,@RequestParam String password,HttpSession session,Model model) {

        Student student= studentRepository.findById(studentId).orElse(null);

        if(student==null || !student.getPassword().equals(password))
        {
            model.addAttribute("error", "Invalid studentid or password");
        
        return "login";
        }

        session.setAttribute("studentId", student.getStudentId());

        return "redirect:/event-list";
    }

    @GetMapping("/event-list")
public String eventList(
        HttpSession session,
        Model model) {

    Integer studentId =
            (Integer) session.getAttribute("studentId");

    if (studentId == null) {
        return "redirect:/";
    }

    List<Event> events = eventRepository.findAll();

    Map<Integer, Long> subEventCounts = new HashMap<>();
    Map<Integer, String> eventStatuses = new HashMap<>();

    for (Event event : events) {

        long count =
                subEventRepository
                        .countByEventId(event.getEventId());

        subEventCounts.put(
                event.getEventId(),
                count
        );

        eventStatuses.put(
                event.getEventId(),
                getEventStatus(event)
        );
    }

    model.addAttribute("events", events);
    model.addAttribute("subEventCounts", subEventCounts);
    model.addAttribute("eventStatuses", eventStatuses);

    return "event-list";
}

    @GetMapping("/register")
    public String registrationPage(@org.springframework.web.bind.annotation.RequestParam int eventId, @RequestParam int subEventId,
Model model,HttpSession session){

     Integer studentId = (Integer) session.getAttribute("studentId");

                 if (studentId == null) {
        return "redirect:/";
    }

        Event event = eventRepository.findById(eventId).orElse(null);

        SubEvent subEvent =
        subEventRepository.findById(subEventId).orElse(null);

if (subEvent == null) {
    return "redirect:/event-list";
}
        model.addAttribute("subEvent", subEvent);
        model.addAttribute("event", event);

        return "register";



    }

    @PostMapping("/register")
public String register(
        @RequestParam int eventId,
        @RequestParam int subEventId,
        HttpSession session,
        Model model) {

    Integer studentId =
            (Integer) session.getAttribute("studentId");

    if (studentId == null) {
        return "redirect:/";
    }

    if (!studentRepository.existsById(studentId)) {

        model.addAttribute("error",
                "Student does not exist");

        return "register";
    }

    if (!eventRepository.existsById(eventId)) {

        model.addAttribute("error",
                "Event does not exist");

        return "register";
    }

    SubEvent subEvent =
            subEventRepository.findById(subEventId)
                    .orElse(null);

    if (subEvent == null) {

        model.addAttribute("error",
                "Sub-event does not exist");

        return "register";
    }

    // Duplicate check

    if (registrationRepository
            .existsByStudentIdAndSubEventId(
                    studentId,
                    subEventId)) {

        model.addAttribute("error",
                "You are already registered for this sub-event");

        model.addAttribute("event",
                eventRepository.findById(eventId).orElse(null));

        model.addAttribute("subEvent", subEvent);

        return "register";
    }

    // Capacity check

    long registrationCount =
            registrationRepository
                    .countBySubEventId(subEventId);

    if (registrationCount >= subEvent.getCapacity()) {

        model.addAttribute("error",
                "This sub-event is already full");

        model.addAttribute("event",
                eventRepository.findById(eventId).orElse(null));

        model.addAttribute("subEvent", subEvent);

        return "register";
    }

    Registration registration =
            new Registration();

    registration.setStudentId(studentId);
    registration.setEventId(eventId);
    registration.setSubEventId(subEventId);

registration.setRegisteredAt(LocalDateTime.now());
registrationRepository.save(registration);

Notification notification = new Notification();

notification.setStudentId(studentId);

notification.setMessage(
        "Registration successful for "
        + subEvent.getSubEventName()
);

notification.setRead(false);

notification.setCreatedAt(LocalDateTime.now());

notificationRepository.save(notification);

return "redirect:/registration-list";
}
    
@GetMapping("/registration-list")
public String registrationList(
        HttpSession session,
        Model model) {

    Integer studentId =
            (Integer) session.getAttribute("studentId");

    if (studentId == null) {
        return "redirect:/";
    }

    List<Notification> notifications =
        notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId);

model.addAttribute("notifications", notifications);

    List<Registration> registrations =
            registrationRepository.findByStudentId(studentId);

    List<Event> events =
            eventRepository.findAll();

    List<SubEvent> subEvents =
            subEventRepository.findAll();

    model.addAttribute("registrations", registrations);
    model.addAttribute("events", events);
    model.addAttribute("subEvents", subEvents);

    return "registration-list";
}

@GetMapping("/logout")
public String Logout(HttpSession session){

session.invalidate();

return "redirect:/";

}

@GetMapping("/subevents")
public String subEvents(
        @RequestParam int eventId,
        HttpSession session,
        Model model) {

    if (session.getAttribute("studentId") == null) {
        return "redirect:/";
    }

    Event event =
            eventRepository.findById(eventId)
                    .orElse(null);

    if (event == null) {
        return "redirect:/event-list";
    }

    List<SubEvent> subEvents =
            subEventRepository.findByEventId(eventId);

    Map<Integer, Long> registrationCounts =
            new HashMap<>();

    for (SubEvent subEvent : subEvents) {

        long count =
                registrationRepository
                        .countBySubEventId(
                                subEvent.getSubEventId());

        registrationCounts.put(
                subEvent.getSubEventId(),
                count);
    }

    model.addAttribute("event", event);
    model.addAttribute("subEvents", subEvents);
    model.addAttribute("registrationCounts",
            registrationCounts);

    return "subevent-list";
}

@GetMapping("/admin/login")
public String adminLoginPage() {
    return "admin-login";
}

@PostMapping("/admin/login")
public String adminLogin(
        @RequestParam String adminName,
        @RequestParam String password,
        HttpSession session,
        Model model) {

    Admin admin = adminRepository.findAll()
            .stream()
            .filter(a -> a.getAdminName().equals(adminName)
                    && a.getPassword().equals(password))
            .findFirst()
            .orElse(null);

    if (admin == null) {
        model.addAttribute("error", "Invalid Admin Name or Password");
        return "admin-login";
    }

    session.setAttribute("adminId", admin.getAdminId());

    return "redirect:/admin/dashboard";
}

@GetMapping("/admin/dashboard")
public String adminDashboard(HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    return "admin-dashboard";
}

@GetMapping("/admin/logout")
public String adminLogout(HttpSession session) {

    session.invalidate();

    return "redirect:/admin/login";
}

@GetMapping("/admin/events")
public String adminEvents(HttpSession session, Model model) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    model.addAttribute("events", eventRepository.findAll());

    return "admin-events";
}

@GetMapping("/admin/events/add")
public String addEventPage(HttpSession session, Model model) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    model.addAttribute("event", new Event());

    return "admin-event-form";
}

@PostMapping("/admin/events/save")
public String saveEvent(
        @RequestParam String eventName,
        @RequestParam String venue,
        @RequestParam String imageUrl,
        @RequestParam String startDate,
        @RequestParam String startTime,
        @RequestParam String endDate,
        @RequestParam String endTime,
        HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    Event event = new Event();

    event.setEventName(eventName);
    event.setVenue(venue);
    event.setImageUrl(imageUrl);

    event.setStartDateTime(
            LocalDateTime.of(
                    LocalDate.parse(startDate),
                    LocalTime.parse(startTime)
            )
    );

    event.setEndDateTime(
            LocalDateTime.of(
                    LocalDate.parse(endDate),
                    LocalTime.parse(endTime)
            )
    );

    eventRepository.save(event);

    return "redirect:/admin/events";
}


@GetMapping("/admin/events/edit")
public String editEventPage(
        @RequestParam int id,
        HttpSession session,
        Model model) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    Event event = eventRepository.findById(id).orElse(null);

    if (event == null) {
        return "redirect:/admin/events";
    }

    model.addAttribute("event", event);

    return "admin-event-form";
}

@PostMapping("/admin/events/update")
public String updateEvent(
        @RequestParam int eventId,
        @RequestParam String eventName,
        @RequestParam String venue,
        @RequestParam String imageUrl,
        @RequestParam String startDate,
        @RequestParam String startTime,
        @RequestParam String endDate,
        @RequestParam String endTime,
        HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    Event event = eventRepository.findById(eventId).orElse(null);

    if (event != null) {

        event.setEventName(eventName);
        event.setVenue(venue);
        event.setImageUrl(imageUrl);

        event.setStartDateTime(
                LocalDateTime.of(
                        LocalDate.parse(startDate),
                        LocalTime.parse(startTime)
                )
        );

        event.setEndDateTime(
                LocalDateTime.of(
                        LocalDate.parse(endDate),
                        LocalTime.parse(endTime)
                )
        );

        eventRepository.save(event);
    }

    return "redirect:/admin/events";
}

@GetMapping("/admin/events/delete")
public String deleteEvent(
        @RequestParam int id,
        HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    eventRepository.deleteById(id);

    return "redirect:/admin/events";
}

@GetMapping("/admin/subevents")
public String adminSubEvents(
        HttpSession session,
        Model model) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    model.addAttribute("subEvents",
            subEventRepository.findAll());

    return "admin-subevents";
}

@GetMapping("/admin/subevents/add")
public String addSubEventPage(
        HttpSession session,
        Model model) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    model.addAttribute("subEvent", new SubEvent());
    model.addAttribute("events", eventRepository.findAll());

    return "admin-subevent-form";
}

@PostMapping("/admin/subevents/save")
public String saveSubEvent(
        @RequestParam String subEventName,
        @RequestParam int eventId,
        @RequestParam int capacity,
        HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    SubEvent subEvent = new SubEvent();

    subEvent.setSubEventName(subEventName);
    subEvent.setEventId(eventId);
    subEvent.setCapacity(capacity);

    subEventRepository.save(subEvent);

    return "redirect:/admin/subevents";
}

@GetMapping("/admin/subevents/edit")
public String editSubEventPage(
        @RequestParam int id,
        HttpSession session,
        Model model) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    SubEvent subEvent =
            subEventRepository.findById(id).orElse(null);

    if (subEvent == null) {
        return "redirect:/admin/subevents";
    }

    model.addAttribute("subEvent", subEvent);
    model.addAttribute("events", eventRepository.findAll());

    return "admin-subevent-form";
}

@PostMapping("/admin/subevents/update")
public String updateSubEvent(
        @RequestParam int subEventId,
        @RequestParam String subEventName,
        @RequestParam int eventId,
        @RequestParam int capacity,
        HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    SubEvent subEvent =
            subEventRepository.findById(subEventId).orElse(null);

    if (subEvent != null) {

        subEvent.setSubEventName(subEventName);
        subEvent.setEventId(eventId);
        subEvent.setCapacity(capacity);

        subEventRepository.save(subEvent);
    }

    return "redirect:/admin/subevents";
}

@GetMapping("/admin/subevents/delete")
public String deleteSubEvent(
        @RequestParam int id,
        HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    subEventRepository.deleteById(id);

    return "redirect:/admin/subevents";
}

@GetMapping("/admin/students")
public String adminStudents(
        HttpSession session,
        Model model) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    model.addAttribute("students",
            studentRepository.findAll());

    return "admin-students";
}

@GetMapping("/admin/students/add")
public String addStudentPage(HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    return "admin-student-form";
}

@PostMapping("/admin/students/save")
public String saveStudent(
        @RequestParam String studentName,
        @RequestParam String department,
        @RequestParam String email,
        @RequestParam String password,
        HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    Student student = new Student();

    student.setStudentName(studentName);
    student.setDepartment(department);
    student.setEmail(email);
    student.setPassword(password);

    studentRepository.save(student);

    return "redirect:/admin/students";
}

@GetMapping("/admin/students/delete")
public String deleteStudent(
        @RequestParam int id,
        HttpSession session) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    List<Registration> registrations =
            registrationRepository.findByStudentId(id);

    registrationRepository.deleteAll(registrations);

    studentRepository.deleteById(id);

    return "redirect:/admin/students";
}

@GetMapping("/admin/registrations")
public String adminRegistrations(
        HttpSession session,
        Model model) {

    if(session.getAttribute("adminId")==null){
        return "redirect:/admin/login";
    }

    model.addAttribute("registrations",
            registrationRepository.findAll());

    model.addAttribute("students",
            studentRepository.findAll());

    model.addAttribute("events",
            eventRepository.findAll());

    model.addAttribute("subEvents",
            subEventRepository.findAll());

    return "admin-registrations";
}

@GetMapping("/admin/reports")
public String adminReports(
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        HttpSession session,
        Model model) {

    if (session.getAttribute("adminId") == null) {
        return "redirect:/admin/login";
    }

    List<Student> students =
            studentRepository.findAll();

    List<Event> events =
            eventRepository.findAll();

    List<SubEvent> subEvents =
            subEventRepository.findAll();

    List<Registration> registrations;

    if (startDate != null && endDate != null
            && !startDate.isEmpty()
            && !endDate.isEmpty()) {

        LocalDateTime start =
                LocalDate.parse(startDate).atStartOfDay();

        LocalDateTime end =
                LocalDate.parse(endDate).atTime(
                        LocalTime.MAX);

        registrations =
                registrationRepository
                        .findByRegisteredAtBetween(
                                start,
                                end);

        model.addAttribute("filtered", true);

    } else {

        registrations =
                registrationRepository.findAll();

        model.addAttribute("filtered", false);
    }

    model.addAttribute(
            "studentCount",
            students.size());

    model.addAttribute(
            "eventCount",
            events.size());

    model.addAttribute(
            "subEventCount",
            subEvents.size());

    model.addAttribute(
            "registrationCount",
            registrations.size());

    Map<Integer, Long> registrationCounts =
            new HashMap<>();

    for (Event event : events) {

        long count = registrations.stream()
                .filter(reg ->
                        reg.getEventId()
                                == event.getEventId())
                .count();

        registrationCounts.put(
                event.getEventId(),
                count);
    }

    model.addAttribute(
            "events",
            events);

    model.addAttribute(
            "registrationCounts",
            registrationCounts);

    model.addAttribute(
            "startDate",
            startDate);

    model.addAttribute(
            "endDate",
            endDate);

    return "admin-reports";
}

@ModelAttribute
public void addNotificationsToModel(
        HttpSession session,
        Model model) {

    Integer studentId =
            (Integer) session.getAttribute("studentId");

    if (studentId != null) {

    // ================= STUDENT NAME =================

    Student student =
            studentRepository.findById(studentId).orElse(null);

    if (student != null) {

        model.addAttribute(
                "studentName",
                student.getStudentName()
        );
    }


    // ================= DASHBOARD STATISTICS =================

    long myRegistrationCount =
            registrationRepository
                    .findByStudentId(studentId)
                    .size();

    long totalSubEventCount =
            subEventRepository.count();

    model.addAttribute(
            "myRegistrationCount",
            myRegistrationCount
    );

    model.addAttribute(
            "totalSubEventCount",
            totalSubEventCount
    );


    // ================= NOTIFICATIONS =================

    List<Notification> notifications =
            notificationRepository
                    .findByStudentIdOrderByCreatedAtDesc(studentId);

    long unreadCount =
            notificationRepository
                    .countByStudentIdAndReadFalse(studentId);

    model.addAttribute(
            "notifications",
            notifications
    );

    model.addAttribute(
            "unreadCount",
            unreadCount
    );
}
}

@GetMapping("/notifications/read")
public String markNotificationsAsRead(HttpSession session) {

    Integer studentId =
            (Integer) session.getAttribute("studentId");

    if (studentId != null) {

        List<Notification> notifications =
                notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId);

        for (Notification notification : notifications) {

            notification.setRead(true);

            notificationRepository.save(notification);
        }
    }

    return "redirect:/event-list";
}

@GetMapping("/unregister/{id}")
public String unregister(
        @PathVariable int id,
        HttpSession session) {

    Integer studentId =
            (Integer) session.getAttribute("studentId");

    // Student must be logged in
    if (studentId == null) {
        return "redirect:/";
    }

    // Find the registration
    Registration registration =
            registrationRepository.findById(id).orElse(null);

    // Registration does not exist
    if (registration == null) {
        return "redirect:/registration-list";
    }

    // Make sure this registration belongs to the logged-in student
    if (registration.getStudentId() != studentId) {
        return "redirect:/registration-list";
    }

    // Find the sub-event before deleting registration
    SubEvent subEvent =
            subEventRepository.findById(
                    registration.getSubEventId()
            ).orElse(null);

    // Delete the registration
    registrationRepository.deleteById(id);

    // Create notification
    Notification notification = new Notification();

    notification.setStudentId(studentId);

    if (subEvent != null) {
        notification.setMessage(
                "You have successfully unregistered from "
                + subEvent.getSubEventName()
        );
    } else {
        notification.setMessage(
                "Your registration has been successfully cancelled"
        );
    }

    notification.setCreatedAt(LocalDateTime.now());

    notificationRepository.save(notification);

    return "redirect:/registration-list";
}

@GetMapping("/notifications/delete/{id}")
public String deleteNotification(
        @PathVariable int id,
        HttpSession session) {

    Integer studentId =
            (Integer) session.getAttribute("studentId");

    // Student must be logged in
    if (studentId == null) {
        return "redirect:/";
    }

    // Delete only if this notification belongs to this student
    notificationRepository.deleteByNotificationIdAndStudentId(
            id,
            studentId
    );

    return "redirect:/event-list";
}

private String getEventStatus(Event event) {

    if (event.getStartDateTime() == null ||
        event.getEndDateTime() == null) {
        return "UNSCHEDULED";
    }

    LocalDateTime now = LocalDateTime.now();

    if (now.isBefore(event.getStartDateTime())) {
        return "UPCOMING";
    }

    if (!now.isAfter(event.getEndDateTime())) {
        return "CURRENT";
    }

    return "CLOSED";
}

@GetMapping("/admin/reports/pdf")
public void generateReportPdf(
        @RequestParam String startDate,
        @RequestParam String endDate,
        HttpSession session,
        HttpServletResponse response) throws IOException {

    if (session.getAttribute("adminId") == null) {
        response.sendRedirect("/admin/login");
        return;
    }

   if (startDate == null || startDate.isEmpty()
        || endDate == null || endDate.isEmpty()) {

    response.sendError(
            HttpServletResponse.SC_BAD_REQUEST,
            "Start date and end date are required."
    );

    return;
}

LocalDateTime start =
        LocalDate.parse(startDate).atStartOfDay();

LocalDateTime end =
        LocalDate.parse(endDate).atTime(LocalTime.MAX);

    List<Registration> registrations =
            registrationRepository.findByRegisteredAtBetween(
                    start, end);

    response.setContentType("application/pdf");
    response.setHeader(
            "Content-Disposition",
            "attachment; filename=CampusConnect_Report.pdf");

    Document document = new Document();

    PdfWriter.getInstance(
            document,
            response.getOutputStream());

    document.open();

    document.add(
            new Paragraph("CAMPUSCONNECT"));
    document.add(
            new Paragraph("COLLEGE EVENT MANAGEMENT SYSTEM"));

    document.add(
            new Paragraph(
                    "Registration Report"));

    document.add(
            new Paragraph(
                    "Period: " +
                    startDate +
                    " to " +
                    endDate));

    document.add(
            new Paragraph(
                    "Total Registrations: " +
                    registrations.size()));

    document.add(new Paragraph(" "));

    Table table = new Table(4);

    table.addCell(new Cell("Registration ID"));
    table.addCell(new Cell("Student ID"));
    table.addCell(new Cell("Event ID"));
    table.addCell(new Cell("Registered At"));

    for (Registration registration :
            registrations) {

        table.addCell(
                new Cell(
                        String.valueOf(
                                registration
                                .getRegistrationId())));

        table.addCell(
                new Cell(
                        String.valueOf(
                                registration
                                .getStudentId())));

        table.addCell(
                new Cell(
                        String.valueOf(
                                registration
                                .getEventId())));

        table.addCell(
                new Cell(
                        String.valueOf(
                                registration
                                .getRegisteredAt())));
    }

    document.add(table);

    document.add(
            new Paragraph(
                    "Generated by CampusConnect Admin"));

    document.close();
}



}