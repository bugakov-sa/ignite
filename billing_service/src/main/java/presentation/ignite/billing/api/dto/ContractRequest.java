package presentation.ignite.billing.api.dto;

import java.util.List;
import java.util.Map;

public class ContractRequest {
    private long id;
    private List<Position> positions;

    public static class Position {
        private long id;
        private Long unionId;
        private String source;
        private String operatorGroup;
        private int trafficType;
        private int recalcRule;
        private Map<String, Long> steps;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Long getUnionId() {
            return unionId;
        }

        public void setUnionId(Long unionId) {
            this.unionId = unionId;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getOperatorGroup() {
            return operatorGroup;
        }

        public void setOperatorGroup(String operatorGroup) {
            this.operatorGroup = operatorGroup;
        }

        public int getTrafficType() {
            return trafficType;
        }

        public void setTrafficType(int trafficType) {
            this.trafficType = trafficType;
        }

        public int getRecalcRule() {
            return recalcRule;
        }

        public void setRecalcRule(int recalcRule) {
            this.recalcRule = recalcRule;
        }

        public Map<String, Long> getSteps() {
            return steps;
        }

        public void setSteps(Map<String, Long> steps) {
            this.steps = steps;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "ContractRequest{" +
                "id=" + id +
                ", positions=" + positions +
                '}';
    }
}
