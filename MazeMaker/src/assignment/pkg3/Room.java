package assignment.pkg3;

public class Room {

    private boolean northDoorOpen;
    private boolean eastDoorOpen;
    private boolean southDoorOpen;
    private boolean westDoorOpen;

    public Room() {
        northDoorOpen = false;
        eastDoorOpen = false;
        southDoorOpen = false;
        westDoorOpen = false;
    }

    public void openDoor(Direction room) {
        switch (room) {
            case NORTH: {
                northDoorOpen = true;
                break;
            }
            case SOUTH: {
                southDoorOpen = true;
                break;
            }
            case WEST: {
                westDoorOpen = true;
                break;
            }
            case EAST: {
                eastDoorOpen = true;
                break;
            }
        }
    }

    public boolean hasOpenDoor() {
        boolean[] doors={northDoorOpen,eastDoorOpen,southDoorOpen,westDoorOpen};
        for(boolean door: doors){
            if(door){
                return true;
            }
        }
        return false;
    }

    public boolean isDoorOpen(Direction door) {
        switch (door) {
            case NORTH: {
                return northDoorOpen;
            }
            case SOUTH: {
                return southDoorOpen;
            }
            case WEST: {
                return westDoorOpen;
            }
            case EAST: {
                return eastDoorOpen;
            }
        }
        return false;
    }
    
    public static void main(String[] args){
        Room room=new Room();
        //System.out.println(""+room.isDoorOpen(Direction.WEST));
        room.openDoor(Direction.WEST);
        room.openDoor(Direction.NORTH);
        System.out.println(""+room.hasOpenDoor());
    }
}
