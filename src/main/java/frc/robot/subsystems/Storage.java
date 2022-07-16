
package frc.robot.subsystems;

// IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


// CODE
public class Storage extends SubsystemBase {

  // CRIANDO OS CONTROLADORES DO SISTEMA DE ARAMAZENADOR
  private WPI_TalonSRX _storage;

  // CRIANDO OS SENSORES DO SISTEMA DE ARMAZENADOR
  private DigitalInput _sensor_sto;

  public Storage() {

    // DEFININDO OS CONTROLADORES DO SISTEMA DE ARMAZENADOR
    _storage = new WPI_TalonSRX(Constants.Motors.Storage._storage);

    // DEFININDO OS SENSORES DO SISTEMA DE ARMAZENADOR
    _sensor_sto = new DigitalInput(Constants.Sensors._sensor_stor);
  }

  // CRIANDO FUNCAO DO ARMAZENADOR
  public void stor(double vel) {
    _storage.set(vel);
  }

  // CRIANDO FUNCAO DOS SENSORES
  public boolean sensorS1() {
    return _sensor_sto.get();
  }

  @Override
  public void periodic() {
    }
  }
