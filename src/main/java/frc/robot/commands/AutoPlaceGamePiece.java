// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.GlobalVariables;
import frc.robot.GlobalVariables.ArmPositions;
import frc.robot.subsystems.ArmSub;
import frc.robot.subsystems.Gripper;

public class AutoPlaceGamePiece extends CommandBase {
  /** Creates a new AdaptiveArmMovement. */
  ArmSub armSub;
  Gripper gripper;
  GlobalVariables.ArmPositions armPosition;
  boolean finished;
  int timer;
  public AutoPlaceGamePiece(ArmSub armSub, Gripper gripper, GlobalVariables.ArmPositions armPosition) {
    this.armSub = armSub;
    this.armPosition = armPosition;
    this.gripper = gripper;
    addRequirements(armSub, gripper);
    finished = false;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    finished = false;
    timer = 0;

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(armPosition == ArmPositions.STOWED_ADAPTIVE){
       armSub.armExtendPresetPositions(armPosition);
        if(armSub.getTelescopePos() <= 10000){
        armSub.armRotPresetPositions(armPosition);
        armSub.jointRotPresetPositions(armPosition);

        }
      }
      else{
       
      armSub.armRotPresetPositions(armPosition);
      armSub.jointRotPresetPositions(armPosition);

      if(armSub.getArmEncoderPosition() >= GlobalVariables.armRotGoal*0.75){
        armSub.armExtendPresetPositions(armPosition);
        if(armSub.getTelescopePos()>= (GlobalVariables.armExtendGoal -500) &&
        armSub.getTelescopePos()<= (GlobalVariables.armExtendGoal + 500)){
          timer++;
          if(/* gripper.getDistanceSensorDist() <= 12 */ timer>=5){
            if(GlobalVariables.gamePiece == 0){
              gripper.moveGripper(0.7);
            }
            else{
              gripper.moveGripper(-0.6);
  
            }
            if (timer >= 20){
              finished = true;
              gripper.moveGripper(0);


            }
          }
          else{
            gripper.moveGripper(0);
          }
        }
        else{
          finished = false;
        }
      }

     
      }

      
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    gripper.moveGripper(0);

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finished;
  }
}
