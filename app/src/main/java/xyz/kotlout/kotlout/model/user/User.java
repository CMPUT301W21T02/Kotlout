package xyz.kotlout.kotlout.model.user;

import java.util.List;
import java.util.UUID;

import xyz.kotlout.kotlout.model.experiment.Experiment;

public class User {
    private UUID uuid;
    private String email;
    private String phoneNumber;
    private List<Experiment> subscriptions;
}
