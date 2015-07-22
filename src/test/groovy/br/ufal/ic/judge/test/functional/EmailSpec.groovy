package br.ufal.ic.judge.test.functional

import groovy.json.JsonSlurper
import spock.lang.Specification

class EmailSpec extends Specification {



    def "Testando email correto"() {
        when:
            EmailTest emailCorrect = new EmailTest('EXCHANGE', 'email')
            emailCorrect.start()
            emailCorrect.call('{"to":"romero.malaquias@gmail.com", "subject": "Test email service", "body": "tchubaruba"}');
            while(emailCorrect.getLoop()){}
        then:
            def jsonSlurper = new JsonSlurper()
            def response = jsonSlurper.parseText(emailCorrect.getResponse())
            response['__status'] == 'SENDED'

    }
    def "Testando email correto com corpo vazio"() {
        when:
        EmailTest emailCorrect = new EmailTest('EXCHANGE', 'email')
        emailCorrect.start()
        emailCorrect.call('{"to":"romero.malaquias@gmail.com", "subject": "Test email service", "body": ""}');
        while(emailCorrect.getLoop()){}
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(emailCorrect.getResponse())
        response['__status'] == 'SENDED'

    }

    def "Testando email invalido"() {
        when:
        EmailTest emailCorrect = new EmailTest('EXCHANGE', 'email')
        emailCorrect.start()
        emailCorrect.call('{"to":"romero.malaquias@gmail.com", "subject": "Test email service", "body": ""}');
        while(emailCorrect.getLoop()){}
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(emailCorrect.getResponse())
        response['__status'] == 'SENDED'

    }

    def "Testando email com campo 'to' vazio"() {
        when:
        EmailTest emailCorrect = new EmailTest('EXCHANGE', 'email')
        emailCorrect.start()
        emailCorrect.call('{"to":"", "subject": "Test email service", "body": "tchubaruba"}');
        while(emailCorrect.getLoop()){}
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(emailCorrect.getResponse())
        response['__status'] == 'FAIL'
        response['__errMSG'] != null

    }

    def "Testando email com campo 'subject' vazio"() {
        when:
        EmailTest emailCorrect = new EmailTest('EXCHANGE', 'email')
        emailCorrect.start()
        emailCorrect.call('{"to":"romero.malaquias@gmail.com", "subject": "", "body": "tchubaruba"}');
        while(emailCorrect.getLoop()){}
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(emailCorrect.getResponse())
        response['__status'] == 'FAIL'
        response['__errMSG'] != null

    }

    def "Testando email com campo 'to' acima do limite"() {
        when:
        EmailTest emailCorrect = new EmailTest('EXCHANGE', 'email')
        emailCorrect.start()
        emailCorrect.call('{"to":"romero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.com", "subject": "Test email service", "body": "tchubaruba"}');
        while(emailCorrect.getLoop()){}
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(emailCorrect.getResponse())
        response['__status'] == 'FAIL'
        response['__errMSG'] != null

    }

    def "Testando email com campo 'subject' acima do limite" () {
        when:
        EmailTest emailCorrect = new EmailTest('EXCHANGE', 'email')
        emailCorrect.start()
        emailCorrect.call('{"to":"romero.malaquias@gmail.com", "subject": "romero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.com", "body": "tchubaruba"}');
        while(emailCorrect.getLoop()){}
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(emailCorrect.getResponse())
        response['__status'] == 'FAIL'
        response['__errMSG'] != null

    }

    def "Testando email com body acima do limite"() {
        when:
        EmailTest emailCorrect = new EmailTest('EXCHANGE', 'email')
        emailCorrect.start()
        emailCorrect.call('{"to":"romero.malaquias@gmail.com", "subject": "Test email service", "body": "romero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.comromero.malaquias@gmail.com"}');
        while(emailCorrect.getLoop()){}
        then:
        def jsonSlurper = new JsonSlurper()
        def response = jsonSlurper.parseText(emailCorrect.getResponse())
        response['__status'] == 'FAIL'
        response['__errMSG'] != null

    }

}
