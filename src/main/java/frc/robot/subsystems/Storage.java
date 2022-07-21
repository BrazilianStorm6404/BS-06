
package frc.robot.subsystems;
// IMPORTS
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


// CODE
public class Storage extends SubsystemBase {

  // CRIANDO OS CONTROLADORES DO SISTEMA DE ARAMAZENADOR
  private VictorSPX ct_feeder;
  private CANSparkMax ct_conveyor;

  // CRIANDO OS SENSORES DO SISTEMA DE ARMAZENADOR
  private DigitalInput sn_photS;

  public Storage() {

    // DEFININDO OS CONTROLADORES DO SISTEMA DE ARMAZENADOR
    try {

      ct_feeder   = new VictorSPX(Constants.Motors.Storage.FEEDER);
      ct_conveyor = new CANSparkMax(Constants.Motors.Storage.CONVEYOR, MotorType.kBrushed);

    } catch (Exception ex) {

      System.out.println("Erro na busca de controlador: " + ex.getStackTrace()[0]);
      
    }

    // DEFININDO OS SENSORES DO SISTEMA DE ARMAZENADOR
    sn_photS = new DigitalInput(Constants.Sensors.STORAGE);
  }

  // CRIANDO FUNCAO DO ARMAZENADOR
  public void setFeeder(double vel) {
    ct_feeder.set(VictorSPXControlMode.PercentOutput, vel);
  }

  public void setConveyor(double vel) {
    ct_conveyor.set(vel);
  }

  // CRIANDO FUNCAO DOS SENSORES
  public boolean sensorS1() {
    return sn_photS.get();
  }

  @Override
  public void periodic() {
    }
  }
