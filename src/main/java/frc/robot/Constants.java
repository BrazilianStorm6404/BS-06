
package frc.robot;

// CODE
public final class Constants {
    
    public static class Motors {

        public static class Drivetrain {
            public static final int _left_front = 14;
            public static final int _left_back = 15;
            public static final int _right_front = 11;
            public static final int _right_back = 16;
        }

        public static class Collector {
            public static final int _collector = 9;
        }

        public static class Storage {
            public static final int _storage = 15;
        }  

        public static class Shooter {
            public static final int _left  = 3;
            public static final int _right = 2;
            
            public static final int _pitch_right = 0; //Servo PWM
            public static final int _pitch_left  = 1; //Servo PWM
            public static final int _yaw         = 17;
        }
        
        public static class Climber {
            public static final int _climber_tube = 11;
            public static final int _climb_rise1  = 12;
            public static final int _climb_rise2  = 13;
        }
    }

    public static class Control_map {
        public static final int _pilot   = 0;
        public static final int _co_pilot = 1;
    }

    public static class Soleinoid {
        public static final int _coletor = 15;
        public static final int _climber = 19;
    }

    public static class Sensors {
        public static final int _limit_up       = 15;
        public static final int _sensor_stor    = 8;
        public static final int _limit_right    = 3;
        public static final int _limit_left     = 4;
        //public static final int _limit_center  = 5;
    }

    public static class Encoders {
        //public static final int _enc_pitch = 0;
        public static final int _enc_shooter = 0;
        public static final int _drive_left  = 1;
        public static final int _drive_right = 2;
    }
}
