import io.jenetics.util.ISeq;

public class Chromosome {
	private ISeq<Chromosome> iSeqGenes;
	private int lenght;
	
	public Chromosome(ISeq<Chromosome> genes) {
		this.iSeqGenes = genes;
		this.lenght = iSeqGenes.length();
	}
	
    public Chromosome getGene(int index) {
        return iSeqGenes.get(index);
    }

    public int length() {
        return iSeqGenes.length();
    }

}
