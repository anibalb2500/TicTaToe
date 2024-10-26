// Room.js
class Room {
    constructor() {
      this.roomId = null;
      this.playerO = null;
      this.playerX = null;
      this.currentPlayer = null;
      this.users = [];    
    }

    coordinates = Array(3).fill().map(() => [null, null, null]);
  
    addUser(user) {
      if (this.isFull()) {
        return;
      }
      console.log(`addUser - ${this.playerX}, ${this.playerO}`);

      this.users.push({ user });
      if (this.playerX == null) {
        console.log('Assigning playerO');
        this.playerX = user;
      } else if (this.playerO == null) {
        console.log('Assigning playerO');
        this.playerO = user;
      }

      // Randomly select the first player and set currentPlayer
      if (this.currentPlayer == null && this.playerX != null && this.playerO != null) {
        console.log('Assigning currentPlayer');
        this.currentPlayer = Math.random() < 0.5 ? this.playerX : this.playerO;
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
      var copy = this.coordinates;
      copy[x][y] = player;
      // if (this.coordinates[x][y] == null) {
      //   console.log()
      //   return false;
      // }

      // this.coordinates[x][y] = player;
      
      // return true;
    }

    getCurrentPlayer() {
      console.log('getCurrentPlayer - ', this.currentPlayer);
      return this.getPlayerByUser(this.currentPlayer);
    }

    updateCurrentPlayer() {
      if (this.currentPlayer != null) {
        // Update to a switch
        if (this.currentPlayer.getId() == this.playerX.getId()) {
          this.currentPlayer = this.playerO
        } else if (this.currentPlayer.getId() == this.playerO.getId()) {
          this.currentPlayer = this.playerX
        }
      }
    }



      /**
   * Get the player type by user.
   * @param {User} user - The user to check.
   * @returns {Player | null} The player type or null if not found.
   */
    getPlayerByUser(user) {
      try {
        if (this.playerO && this.playerO.getId() === user.getId()) {
          return 'O';
        } else if (this.playerX && this.playerX.getId() === user.getId()) {
          return 'X';
        }
      } catch (error) {}

      return null;
    }
  }
  
  export default Room;