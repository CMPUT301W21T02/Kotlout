package xyz.kotlout.kotlout.model.experiment;

import java.util.List;

import xyz.kotlout.kotlout.model.user.User;

public abstract class Experiment {
    private User owner;
    private String description;
    private String region;
    private int minimumTrials;
    private boolean isOngoing;
    private boolean geolocationRequired;
    private List<User> ignoredUsers;
    private List<Post> posts;
}
