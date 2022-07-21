
package frc.robot.subsystems;

// IMPORTS
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

// CODE
public class Climber extends SubsystemBase {
  
  // CRIANDO OS CONTROLADORES DO SISTEMA DE ESCALADA
  private VictorSPX ct_tube1, ct_tube2, at_rSolEt1, at_rSolEt2, at_lSolEt1, at_lSolEt2;

  public Climber() {

    // DEFININDO OS CONTROLADORS DO SISTEMA DE ESCALADA
    try {

      ct_tube1 = new VictorSPX(Constants.Motors.Climber.CLIMBER_LEFT_TUBE);
      ct_tube2 = new VictorSPX(Constants.Motors.Climber.CLIMBER_RIGHT_TUBE);

      at_rSolEt1 = new VictorSPX(Constants.Soleinoid.CLIMBER);
      at_rSolEt2 = new VictorSPX(Constants.Soleinoid.CLIMBER);
      at_lSolEt1 = new VictorSPX(Constants.Soleinoid.CLIMBER);
      at_lSolEt2 = new VictorSPX(Constants.Soleinoid.CLIMBER);

      ct_tube1.follow(ct_tube2);
      at_lSolEt1.follow(at_rSolEt1);
      at_lSolEt2.follow(at_rSolEt2);

    } catch (Exception ex) {
      
      System.out.println("Erro na busca de controlador");

    }
  }

  // FUNCAO DO SISTEMA DE ESCALADA
  public void climbing(double ct) {

    ct_tube1.set(ControlMode.PercentOutput, ct);

  }

  public void climberSolenoidET1 (Boolean activate){
    
    at_rSolEt1.set(ControlMode.PercentOutput, activate ? 1.0 : 0);

  }

  public void climberSolenoidET2 (Boolean activate){
    
    at_rSolEt2.set(ControlMode.PercentOutput, activate ? 1.0 : 0);

  }

  @Override
  public void periodic() {

  }
}
//*/