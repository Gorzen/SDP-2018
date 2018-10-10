package ch.epfl.sweng.studyup.tequila;

/**
 * Tequila user profile information.
 *
 * @author Solal Pirelli
 * Modified by us
 */
public final class Profile {
    /**
     * This is the user ID, it is guaranteed to be unique.
     */
    private final String sciper;
    /**
     * This is probably unique, but you shouldn't depend on it.
     */
    private final String gaspar;
    /**
     * Don't spam your users! Use this carefully.
     */
    private final String email;
    /**
     * Do not assume anything about what exactly this contains.
     * Some people have one name, some have multiple, some have honorary prefixes, ...
     */
    private final String firstNames;
    /**
     * Same remark as `firstNames`.
     */
    private final String lastNames;

    /** Constructor for a Profile. Must not be empty.
     *
     * @param sciper This is the user ID, it is guaranteed to be unique.
     * @param gaspar This is probably unique, but you shouldn't depend on it.
     * @param email Don't spam your users! Use this carefully.
     * @param firstNames Same remark as `firstNames`.
     * @param lastNames Same remark as `firstNames`.
     */
    public Profile(String sciper, String gaspar, String email, String firstNames, String lastNames) {
        if(sciper != null && !sciper.isEmpty()) {
            this.sciper = sciper;
        } else {
            this.sciper = null;
        }

        if(gaspar != null && !gaspar.isEmpty()) {
            this.gaspar = gaspar;
        } else {
            this.gaspar = null;

        }

        if(email != null && !email.isEmpty()) {
            this.email = email;
        } else {
            this.email = null;

        }

        if(firstNames != null && !firstNames.isEmpty()) {
            this.firstNames = firstNames;
        } else {
            this.firstNames = null;

        }

        if(lastNames != null && !lastNames.isEmpty()) {
            this.lastNames = lastNames;
        } else {
            this.lastNames = null;

        }
    }

    public String getSciper() {
        return sciper;
    }

    public String getGaspar() {
        return gaspar;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public String getLastNames() {
        return lastNames;
    }

    @Override
    public String toString() {
        return firstNames + " " + lastNames
                + "\nsciper: " + sciper
                + "\ngaspar: " + gaspar
                + "\nemail: " + email;
    }
}
