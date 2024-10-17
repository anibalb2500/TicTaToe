import User from './user.js';

class UserTracker {
    constructor() {
      /** @type {User[]} */
      this.users = [];
    }
  
    /**
     * Add a user to the tracker.
     * @param {User} user - The user to add.
     */
    addUser(user) {
      this.users.push(user);
    }
  
    /**
     * Remove a user from the tracker by ID.
     * @param {string} id - The ID of the user to remove.
     */
    removeUser(id) {
      this.users = this.users.filter(user => user.getId() !== id);
    }
  
    /**
     * Get the number of users in the tracker.
     * @returns {number} The number of users.
     */
    getUserCount() {
      return this.users.length;
    }
  
    /**
     * Get the list of users in the tracker.
     * @returns {User[]} The list of users.
     */
    getUsers() {
      return this.users;
    }

        /**
     * Get a user by ID.
     * @param {string} username - The username of the user to get.
     * @returns {User | undefined} The user with the matching ID, or undefined if not found.
     */
    getUserByName(username) {
      return this.users.find(user => user.getUsername() === username);
    }
  }
  
  export default UserTracker;