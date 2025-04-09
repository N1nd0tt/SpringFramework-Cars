package org.example.models;

import lombok.*;
import jakarta.persistence.*;
@Entity
@Table(name = "rental")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {
    @Id
    @Column(nullable = false, unique = true)
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "rent_date", nullable = false)
    @Getter
    private String rentDate;
    @Column(name = "return_date")
    @Getter
    private String returnDate;
}
