// User.js
class User {
    /**
     * Create a user.
     * @param {string} id - The user's socket ID.
     * @param {string} username - The user's username.
     */
    constructor(id, username, player) {
      /** @type {string} */
      this.id = id;
      /** @type {string} */
      this.username = username;
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
  }

  export default User;
  