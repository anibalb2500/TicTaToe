// Room.js
class Room {
    constructor() {
      this.users = [];
      this.coordinates = Array(3).fill().map(() => [null, null, null]);
    }
  
    addUser(id, player) {
      this.users.push({ id, player });
    }
  
    removeUser(id) {
      this.users = this.users.filter(user => user.id !== id);
    }
  
    isFull() {
      return this.users.length >= 2;
    }
  
    isEmpty() {
      return this.users.length === 0;
    }
  
    getPlayerCount() {
      return this.users.length;
    }
  
    getCoordinates() {
      return this.coordinates;
    }
  
    setCoordinate(x, y, player) {
      this.coordinates[x][y] = player;
    }
  }
  
  export default Room;