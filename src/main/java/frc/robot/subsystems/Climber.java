
package frc.robot.subsystems;

// IMPORTS
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

// CODE
public class Climber extends SubsystemBase {
  
  // CRIANDO OS CONTROLADORES DO SISTEMA DE ESCALADA
  private VictorSPX at_rSol, at_lSol;

  public Climber() {
/*
    // DEFININDO OS CONTROLADORS DO SISTEMA DE ESCALADA
    try {
      at_rSol = new VictorSPX(Constants.Soleinoid.CLIMBER);
      at_lSol = new VictorSPX(Constants.Soleinoid.CLIMBER);

      at_lSol.follow(at_rSol);

    } catch (Exception ex) {
      
      System.out.printf("\n\nERRO NA BUSCA DE CONTROLADOR, LINHA: %s\n\n", ex.getStackTrace()[0]);
      return;

    }*/
  }

  // FUNCAO DO SISTEMA DE ESCALADA
  public void climber (Boolean activate){
    
    at_rSol.set(ControlMode.PercentOutput, activate ? 1.0 : 0);

  }

  @Override
  public void periodic() {

  }
}
//*/