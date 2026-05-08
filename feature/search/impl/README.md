# `:feature:search:impl`

## Module dependency graph

<!--region graph-->
```mermaid
---
config:
  layout: elk
  elk:
    nodePlacementStrategy: SIMPLE
---
graph TB
  subgraph :feature
    direction TB
    subgraph :feature:search
      direction TB
      :feature:search:api[api]:::android-library
      :feature:search:impl[impl]:::android-library
    end
    subgraph :feature:interests
      direction TB
      :feature:interests:api[api]:::android-library
    end
    subgraph :feature:topic
      direction TB
      :feature:topic:api[api]:::android-library
    end
  end
  subgraph :core
    direction TB
    :core:analytics[analytics]:::android-library
    :core:common[common]:::jvm-library
    :core:data[data]:::android-library
    :core:database[database]:::android-library
    :core:datastore[datastore]:::android-library
    :core:datastore-proto[datastore-proto]:::jvm-library
    :core:designsystem[designsystem]:::android-library
    :core:domain[domain]:::android-library
    :core:model[model]:::jvm-library
    :core:navigation[navigation]:::android-library
    :core:network[network]:::android-library
    :core:notifications[notifications]:::android-library
    :core:ui[ui]:::android-library
  end
  :lint[lint]:::android-library

  :core:analytics -.-> :lint
  :core:data -.-> :core:analytics
  :core:data --> :core:common
  :core:data --> :core:database
  :core:data --> :core:datastore
  :core:data --> :core:network
  :core:data -.-> :core:notifications
  :core:data -.-> :lint
  :core:database --> :core:model
  :core:database -.-> :lint
  :core:datastore -.-> :core:common
  :core:datastore --> :core:datastore-proto
  :core:datastore --> :core:model
  :core:datastore -.-> :lint
  :core:designsystem -.-> :lint
  :core:domain --> :core:data
  :core:domain --> :core:model
  :core:domain -.-> :lint
  :core:navigation -.-> :lint
  :core:network --> :core:common
  :core:network --> :core:model
  :core:network -.-> :lint
  :core:notifications -.-> :core:common
  :core:notifications --> :core:model
  :core:notifications -.-> :lint
  :core:ui --> :core:analytics
  :core:ui --> :core:designsystem
  :core:ui --> :core:model
  :core:ui -.-> :lint
  :feature:interests:api --> :core:navigation
  :feature:interests:api -.-> :lint
  :feature:search:api -.-> :core:domain
  :feature:search:api --> :core:navigation
  :feature:search:api -.-> :lint
  :feature:search:impl -.-> :core:designsystem
  :feature:search:impl -.-> :core:domain
  :feature:search:impl -.-> :core:ui
  :feature:search:impl -.-> :feature:interests:api
  :feature:search:impl -.-> :feature:search:api
  :feature:search:impl -.-> :feature:topic:api
  :feature:search:impl -.-> :lint
  :feature:topic:api -.-> :core:designsystem
  :feature:topic:api --> :core:navigation
  :feature:topic:api -.-> :core:ui
  :feature:topic:api -.-> :lint

classDef android-application fill:#CAFFBF,stroke:#000,stroke-width:2px,color:#000;
classDef android-feature fill:#FFD6A5,stroke:#000,stroke-width:2px,color:#000;
classDef android-library fill:#9BF6FF,stroke:#000,stroke-width:2px,color:#000;
classDef android-test fill:#A0C4FF,stroke:#000,stroke-width:2px,color:#000;
classDef jvm-library fill:#BDB2FF,stroke:#000,stroke-width:2px,color:#000;
classDef unknown fill:#FFADAD,stroke:#000,stroke-width:2px,color:#000;
```

<details><summary>📋 Graph legend</summary>

```mermaid
graph TB
  application[application]:::android-application
  feature[feature]:::android-feature
  library[library]:::android-library
  jvm[jvm]:::jvm-library

  application -.-> feature
  library --> jvm

classDef android-application fill:#CAFFBF,stroke:#000,stroke-width:2px,color:#000;
classDef android-feature fill:#FFD6A5,stroke:#000,stroke-width:2px,color:#000;
classDef android-library fill:#9BF6FF,stroke:#000,stroke-width:2px,color:#000;
classDef android-test fill:#A0C4FF,stroke:#000,stroke-width:2px,color:#000;
classDef jvm-library fill:#BDB2FF,stroke:#000,stroke-width:2px,color:#000;
classDef unknown fill:#FFADAD,stroke:#000,stroke-width:2px,color:#000;
```

</details>
<!--endregion-->
