package com.mjuAppSW.joA.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="Log")
public class Log {
    @Id
    @Column(name="Log_id")
    private String id;

    @Column(name="Entry_date")
    private Timestamp date;

    private String logger;

    @Column(name="Log_level")
    private String level;

    private String message;

    private String exception;
}