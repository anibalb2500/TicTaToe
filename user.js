// User.js
class User {
    /**
     * Create a user.
     * @param {string} id - The user's socket ID.
     * @param {string} username - The user's username.
     * @param {Player} [player] - The player's type (X or O).
     */
    constructor(id, username, player) {
      /** @type {string} */
      this.id = id;
      /** @type {string} */
      this.username = username;
      /** @type {Player} */
      this.player = player;
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
     * Get the player's type.
     * @returns {Player} The player's type.
     */
    getPlayer() {
      return this.player;
    }
  }

  export default User;
  