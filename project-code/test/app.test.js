import chai from 'chai';
import chaiHttp from 'chai-http';
import app from '/app/app.js'; // Import the Express app

chai.use(chaiHttp);
const { expect } = chai;

describe('Express App Tests', () => {
    it('should return "Hello I am Adham and this is nodejs website" on GET /', (done) => {
        chai.request(app)
            .get('/')
            .end((err, res) => {
                expect(res).to.have.status(200);
                expect(res.text).to.equal('Hello I am Adham and this is nodejs website');
                done();
            });
    });
});

