package frc.megiddolions.lib.annotations;

public @interface UsedInCompetition {
    String competition() default "";
    String year();
    String team() default "5038";
}
