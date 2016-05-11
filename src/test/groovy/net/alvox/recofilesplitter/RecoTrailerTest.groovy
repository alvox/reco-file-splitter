package net.alvox.recofilesplitter

import spock.lang.Specification

class RecoTrailerTest extends Specification {

    def "test trailer calculation with dt only"() {
        setup:
        def trailer = new RecoTrailer()

        when:
        trailer.updateAmounts("****************************************************************10000000574")
        trailer.updateAmounts("****************************************************************10000000555")
        trailer.updateAmounts("****************************************************************10000001486")

        then:
        trailer.compose() == "900000000300000000000261E00000000000000000000000{                                                                                                                    "
    }

    def "test trailer calculation with ct only"() {
        setup:
        def trailer = new RecoTrailer()

        when:
        trailer.updateAmounts("****************************************************************14000006544")
        trailer.updateAmounts("****************************************************************14000003649")
        trailer.updateAmounts("****************************************************************14000000705")

        then:
        trailer.compose() == "900000000000000000000000{00000000300000000001089Q                                                                                                                    "
    }

    def "test trailer calculation with mixed dt and ct"() {
        setup:
        def trailer = new RecoTrailer()

        when:
        trailer.updateAmounts("****************************************************************10000000574")
        trailer.updateAmounts("****************************************************************14000000705")
        trailer.updateAmounts("****************************************************************14000006544")
        trailer.updateAmounts("****************************************************************10000000555")

        then:
        trailer.compose() == "900000000200000000000112I00000000200000000000724R                                                                                                                    "
    }

}
