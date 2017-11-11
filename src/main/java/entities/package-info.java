/**
 * Created by oradchykova on 8/22/17.
 */

@org.hibernate.annotations.GenericGenerator(
        name = "USER_ID_GENERATOR",
        strategy = "enhanced-sequence",
        parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "sequence_name",
                        value = "User_Sequence"
                ),
                @org.hibernate.annotations.Parameter(
                        name = "initial_value",
                        value = "1000"
                )
        }
)
package entities;