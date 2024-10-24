// Users who are not in a room yet. Waiting on game.
class Lobby {
    constructor() {
      this.users = [];
    }
  
    addUser(id) {
      this.users.push(id);
    }
  
    removeUser(id) {
      this.users = this.users.filter(user => user !== id);
    }
  
    getUserCount() {
      return this.users.length;
    }
  
    getUsers() {
      return this.users;
    }
  }
  
  export default Lobby;