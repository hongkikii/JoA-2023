package com.mjuAppSW.joA.geography.location;

import com.mjuAppSW.joA.geography.college.PCollege;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @Column(name = "Member_id")
    private Long id; // 멤버 아이디와 동일하게

    @ManyToOne
    @JoinColumn(name = "College_id")
    private PCollege college;

    @Column(name = "Member_point", columnDefinition = "geometry(PointZ, 4326)")
    private Point point;

    @Column(name = "Is_contained")
    private Boolean isContained;

    public Location(Long id, PCollege college) {
        this.id = id;
        this.college = college;
        this.isContained = false;
    }

    public Location(Long id, PCollege college, Point point, Boolean isContained) {
        this.id = id;
        this.college = college;
        this.point = point;
        this.isContained = isContained;
    }
}
