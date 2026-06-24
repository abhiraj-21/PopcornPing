package com.abhiraj.PopcornPing.domain.entities;

import com.abhiraj.PopcornPing.domain.enums.NotificationStatus;
import com.abhiraj.PopcornPing.domain.enums.WatchStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "watch_list")
@Table(
        name = "user_movie_tracker",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_movie",
                        columnNames = {"user_id", "movie_id"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserMovieTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Enumerated(EnumType.STRING)
    @Column(name = "watch_status", nullable = false)
    private WatchStatus watchStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_status", nullable = false)
    private NotificationStatus notificationStatus;

    @Column
    private String calendarEventId;

}
