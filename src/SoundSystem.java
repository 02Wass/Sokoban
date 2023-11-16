public interface SoundSystem {
    /** Run code depending on the given event ID, intended for audio playback
     * @param eventID The ID of the event
     */
    public abstract void soundEvent(int eventID);
}