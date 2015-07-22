package br.ufal.ic.judge.test.functional

import br.ufal.ic.judge.commons.ClientRPC

/**
 * Created by huxley on 18/07/15.
 */
class EmailTest extends ClientRPC {
    private String response

    EmailTest(String exchangeName, String key) {
        super(exchangeName, key);
    }

    public void close() {
        exchange.closeConnection();
        this.listen = false
    }

    void doWork (String message){
        response = message
        this.close()
    }

    public String getResponse() {
        return response
    }

    public boolean getLoop() {
        return this.listen
    }

    public static void main(String[] argv) {
        EmailTest fibonacciRpc = new EmailTest("EXCHANGE", "email");
        fibonacciRpc.start();
        fibonacciRpc.call('{"to":"romero.malaquias@gmail.com", "subject": "Test email service", "body": "tchubaruba"}');

    }

}





