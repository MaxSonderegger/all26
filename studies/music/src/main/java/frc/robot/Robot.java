
package frc.robot;

import org.team100.lib.music.commands.Play;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    private final Play play;

    public Robot() {
        play = new Play("popcorn2.mid", null);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        CommandScheduler.getInstance().schedule(play);
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void disabledExit() {
        CommandScheduler.getInstance().cancelAll();

    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void teleopExit() {
    }

}
