
package frc.robot;

// CODE
public final class Constants {
    
    public static class Motors {

        public static class Drivetrain {
            public static final int LEFT_FRONT  = 14;
            public static final int LEFT_BACK   = 15;
            public static final int RIGHT_FRONT = 11;
            public static final int RIGHT_BACK  = 16;
        }

        public static class Collector {
            public static final int COLLECTOR = 7;
        }

        public static class Storage {
            public static final int FEEDER   = 5;
            public static final int CONVEYOR = 5;

        }  

        public static class Shooter {
            public static final int LEFT  = 3;
            public static final int RIGHT = 2;
            
            public static final int PITCH_RIGHT = 0; //Servo PWM
            public static final int PITCH_LEFT  = 1; //Servo PWM
            public static final int YAW         = 17;
        }
        
        public static class Climber {
            public static final int CLIMBER_LEFT_TUBE  = 11;
            public static final int CLIMBER_RIGHT_TUBE = 11;
            public static final int CLIMB_RISE1  = 12;
            public static final int CLIMB_RISE2  = 13;
        }
    }

    public static class Control_map {
        public static final int PILOT    = 0;
        public static final int CO_PILOT = 1;
    }

    public static class Soleinoid {
        public static final int COLETOR = 15;
        public static final int CLIMBER = 19;
    }

    public static class Sensors {
        public static final int STORAGE      = 8;
        public static final int LIMIT_RIGHT  = 3;
        public static final int LIMIT_LEFT   = 4;
        public static final int LIMIT_CENTER = 5;
    }

    public static class Encoders {
        public static final int PITCH       = 0;
        public static final int SHOOTER     = 0;
        public static final int DRIVE_LEFT  = 1;
        public static final int DRIVE_RIGHT = 2;
    }
}
