// Room.js
class Room {
    constructor() {
      this.roomId = 0;
      this.playerO = null;
      this.playerX = null;
      this.users = [];
      this.coordinates = Array(3).fill().map(() => [null, null, null]);
    }
  
    addUser(user) {
      if (this.isFull()) {
        return;
      }

      this.users.push({ user });
      if (this.playerX == null) {
        this.playerX = user;
      } else if (this.playerO == null) {
        this.playerO = user;
      }
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

    // First Player is always X
    getPlayerX() {
      if (this.isEmpty()) {
        return null;
      }
      return this.users[0].id;
    }

    // Second Player is always O
    getPlayerX() {
      if (this.users.length < 2) {
        return null;
      }
      return this.users[1].id;
    }

      /**
   * Get the player type by user.
   * @param {User} user - The user to check.
   * @returns {Player | null} The player type or null if not found.
   */
    getPlayerByUser(user) {
      console.log(`getPlayerByUser - ${user}`);
      console.log(`playerO - ${this.playerO}`);
      console.log(`playerX - ${this.playerX}`);

      console.log('5')
      if (this.playerO != null && this.playerO == user) {
        console.log('6')
        return 'O';
      } else if (this.playerX != null && this.playerX.id == user.id) {
        console.log('7')
        return 'X';
      }

      console.log('8')
      return null;
    }
  }
  
  export default Room;