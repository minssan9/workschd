package com.voyagerss.service;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarRequestInitializer;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleCalendarService {

    @Value("${spring.api-key.google.google-calendar-api.key}")
    private String GOOGLE_CALENDAR_API_KEY;

    private final static String GOOGLE_KOREA_HOLIDAY_CALENDAR_ID = "en.south_korea#holiday@group.v.calendar.google.com";
    private final static JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


    private Calendar calendarService;

    public GoogleCalendarService(Calendar calendarService) {
        this.calendarService = calendarService;
    }

    public Event createEvent(String summary, String description, String startDateTime, String endDateTime) throws IOException {
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        DateTime start = new DateTime(startDateTime);
        EventDateTime startEventDateTime = new EventDateTime().setDateTime(start);
        event.setStart(startEventDateTime);

        DateTime end = new DateTime(endDateTime);
        EventDateTime endEventDateTime = new EventDateTime().setDateTime(end);
        event.setEnd(endEventDateTime);

        return calendarService.events().insert("primary", event).execute();
    }


    /**
     * Google calendar API를 사용하기 위한 Calendar 서비스를 생성해서 반환하는 함수
     *
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    private Calendar createGoogleCalendarService() throws GeneralSecurityException, IOException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        return new Calendar.Builder(httpTransport, JSON_FACTORY, null)
                .setCalendarRequestInitializer(new CalendarRequestInitializer(GOOGLE_CALENDAR_API_KEY))
                .build();
    }

    /**
     * 공휴일 데이터를 가져오는 API
     *
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public Events findHolidayFromGoogleCalendar() throws IOException, GeneralSecurityException {
        Calendar service = createGoogleCalendarService();

        return service.events().list(GOOGLE_KOREA_HOLIDAY_CALENDAR_ID).execute();
    }
}
