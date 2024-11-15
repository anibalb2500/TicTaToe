// User.js
class User {
    /**
     * Create a user.
     * @param {string} id - The user's socket ID.
     * @param {string} username - The user's username.
     * @param {string} currentRoomId - The user's current room ID. 
     */
    constructor(id, username) {
      /** @type {string} */
      this.id = id;
      /** @type {string} */
      this.username = username;
      /** @type {string} */
      this.currentRoomId = null;
    }
  
    /**
     * Get the user's ID.
     * @returns {string} The user's ID.
     */
    getId() {
      return this.id;
    }
  
    /**
     * Get the user's username.
     * @returns {string} The user's username.
     */
    getUsername() {
      return this.username;
    }

    /**
     * Get the user's current room ID.
     * @returns {string} The user's current room ID.
     */
    getCurrentRoomId() {
      return this.currentRoomId;
    }
  }

  export default User;
  